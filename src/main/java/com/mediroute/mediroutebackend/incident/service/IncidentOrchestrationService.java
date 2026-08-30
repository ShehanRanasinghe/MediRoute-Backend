package com.mediroute.mediroutebackend.incident.service;

import com.mediroute.mediroutebackend.allocation.model.AllocationResult;
import com.mediroute.mediroutebackend.allocation.service.AllocationService;
import com.mediroute.mediroutebackend.common.models.*;
import com.mediroute.mediroutebackend.common.models.repository.*;
import com.mediroute.mediroutebackend.decision.model.HospitalRecommendation;
import com.mediroute.mediroutebackend.decision.model.RecommendationRequest;
import com.mediroute.mediroutebackend.decision.model.RecommendationResult;
import com.mediroute.mediroutebackend.decision.service.RecommendationService;
import com.mediroute.mediroutebackend.incident.model.*;
import com.mediroute.mediroutebackend.network.model.CriticalNodeResult;
import com.mediroute.mediroutebackend.network.service.NetworkAnalysisService;
import com.mediroute.mediroutebackend.optimization.model.OptimizationResult;
import com.mediroute.mediroutebackend.optimization.service.OptimizationService;
import com.mediroute.mediroutebackend.routing.model.RouteRequest;
import com.mediroute.mediroutebackend.routing.model.RouteResult;
import com.mediroute.mediroutebackend.routing.service.RoutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * UPDATED from the original version. Three fixes in this revision:
 *
 * 1. DISPATCH PLAN PERSISTENCE: the optimization decision is now saved to
 *    dispatch_plan / dispatch_plan_item (previously computed and discarded).
 *
 * 2. REAL DEPLETION: when an incident is allocated an ambulance, one actual
 *    Resource row is flipped from AVAILABLE to IN_USE. When supply items
 *    are dispatched, they're flipped from PENDING to LOADED. Previously
 *    NOTHING changed state, so availability numbers never actually dropped
 *    no matter how many incidents were processed - this is what makes "all
 *    ambulances booked" a real, demonstrable scenario now instead of an
 *    impossible one.
 *
 * 3. NO-AMBULANCE HANDLING: if no ambulance is available, the optimization
 *    step is skipped entirely (no point planning a loadout for a vehicle
 *    that doesn't exist) and the response says so explicitly via
 *    DispatchPlanView.note, instead of silently returning misleading data.
 */
@Service
public class IncidentOrchestrationService {

    @Autowired private PatientIncidentRepository patientIncidentRepository;
    @Autowired private HospitalRepository hospitalRepository;
    @Autowired private AmbulanceDepotRepository ambulanceDepotRepository;
    @Autowired private SupplyItemRepository supplyItemRepository;
    @Autowired private ResourceRepository resourceRepository;
    @Autowired private DispatchPlanRepository dispatchPlanRepository;
    @Autowired private DispatchPlanItemRepository dispatchPlanItemRepository;

    @Autowired private RecommendationService recommendationService;
    @Autowired private RoutingService routingService;
    @Autowired private AllocationService allocationService;
    @Autowired private OptimizationService optimizationService;
    @Autowired private NetworkAnalysisService networkAnalysisService;

    private static final int DEFAULT_VEHICLE_CAPACITY = 15;

    public IncidentResponse handleNewIncident(IncidentReportRequest request) {
        long overallStart = System.nanoTime();

        // Step 1: persist the incident as PENDING
        PatientIncident incident = new PatientIncident();
        incident.setPatientReference(request.getPatientReference());
        incident.setPhoneNumber(request.getPhoneNumber());
        incident.setLatitude(request.getLatitude());
        incident.setLongitude(request.getLongitude());
        incident.setConditionType(request.getConditionType());
        incident.setSeverityScore(request.getSeverityScore());
        incident.setStatus(IncidentStatus.PENDING);
        incident = patientIncidentRepository.save(incident);

        // Step 2: Decision module - find the best matching hospital
        RecommendationRequest recRequest = new RecommendationRequest();
        recRequest.setConditionType(request.getConditionType());
        recRequest.setPatientLatitude(request.getLatitude());
        recRequest.setPatientLongitude(request.getLongitude());
        recRequest.setTopK(1);
        RecommendationResult recResult = recommendationService.recommend(recRequest, "heap");
        HospitalRecommendation bestHospital = recResult.getRankedHospitals().isEmpty()
                ? null : recResult.getRankedHospitals().get(0);

        // Step 3: Routing module - plan the ambulance route from the depot
        RouteResult routeResult = null;
        if (bestHospital != null) {
            Hospital hospital = findHospitalById(bestHospital.getHospitalId());
            AmbulanceDepot depot = ambulanceDepotRepository.findAllWithNode().stream().findFirst().orElse(null);

            if (hospital != null && depot != null) {
                RouteRequest routeRequest = new RouteRequest();
                routeRequest.setSourceId(depot.getNode().getId());
                routeRequest.setDestinationId(hospital.getNode().getId());
                routeRequest.setAlgorithm("astar");
                routeResult = routingService.computeRoute(routeRequest);
            }
        }

        // Step 4: Allocation module - does THIS incident get an ambulance?
        AllocationResult allocationResult = allocationService.runAllocation(ResourceType.AMBULANCE, "greedy");
        boolean thisIncidentAllocated = allocationResult.getSelectedIncidentIds().contains(incident.getId());

        // Step 4b: if allocated, actually RESERVE one physical ambulance -
        // this is the state change that was previously missing entirely.
        DispatchPlanView dispatchPlan;
        if (thisIncidentAllocated) {
            Resource reservedAmbulance = resourceRepository
                    .findByResourceTypeAndStatus(ResourceType.AMBULANCE, ResourceStatus.AVAILABLE)
                    .stream().findFirst().orElse(null);

            if (reservedAmbulance != null) {
                reservedAmbulance.setStatus(ResourceStatus.IN_USE);
                resourceRepository.save(reservedAmbulance);

                incident.setStatus(IncidentStatus.ASSIGNED);
                incident.setAssignedResource(reservedAmbulance);
                incident.setAssignedHospital(bestHospital != null ? findHospitalById(bestHospital.getHospitalId()) : null);
                patientIncidentRepository.save(incident);

                // Step 5: Optimization module - only meaningful once a real
                // vehicle has actually been reserved for this trip.
                OptimizationResult optimizationResult = optimizationService.optimize(DEFAULT_VEHICLE_CAPACITY, "dp");
                dispatchPlan = persistAndBuildDispatchPlan(incident, optimizationResult);
            } else {
                // Safety net: AllocationService said "allocated" based on a
                // count, but no physical AVAILABLE resource actually exists.
                // Should not normally happen (the count IS derived from this
                // same query) but guarded here rather than trusting that.
                thisIncidentAllocated = false;
                dispatchPlan = emptyDispatchPlan("No ambulance resource was actually available - please try again.");
            }
        } else {
            dispatchPlan = emptyDispatchPlan("Not applicable - no ambulance was assigned to this incident.");
        }

        // Step 6: Network Analysis module - safety check on the chosen route
        boolean routeUsesCriticalNode = false;
        if (routeResult != null && routeResult.isPathFound()) {
            CriticalNodeResult criticalNodes = networkAnalysisService.findCriticalNodes();
            routeUsesCriticalNode = routeResult.getPath().stream()
                    .anyMatch(criticalNodes.getCriticalNodeIds()::contains);
        }

        long overallEnd = System.nanoTime();

        IncidentResponse response = new IncidentResponse();
        response.setIncidentId(incident.getId());
        response.setRecommendedHospital(bestHospital);
        response.setRoute(routeResult);
        response.setAmbulanceAllocated(thisIncidentAllocated);
        response.setDispatchPlan(dispatchPlan);
        response.setRouteUsesCriticalNode(routeUsesCriticalNode);
        response.setOverallProcessingTimeNanos(overallEnd - overallStart);
        return response;
    }

    public DashboardSummary getDashboardSummary() {
        DashboardSummary summary = new DashboardSummary();

        summary.setPendingIncidents(patientIncidentRepository.findByStatus(IncidentStatus.PENDING).size());
        summary.setOngoingIncidents(patientIncidentRepository.findByStatus(IncidentStatus.ASSIGNED).size());

        // FIX: previously read AmbulanceDepot.availableAmbulances, a static
        // seeded number that never changed. Now counts AVAILABLE Resource
        // rows directly - the SAME source AllocationService itself uses -
        // so the dashboard and the actual allocation decisions can never
        // disagree with each other.
        int availableAmbulances = resourceRepository
                .findByResourceTypeAndStatus(ResourceType.AMBULANCE, ResourceStatus.AVAILABLE)
                .size();
        summary.setAvailableAmbulances(availableAmbulances);

        summary.setCriticalNodeCount(networkAnalysisService.findCriticalNodes().getCriticalNodeIds().size());

        List<HospitalStatusView> hospitals = hospitalRepository.findAllWithNode().stream()
                .map(h -> new HospitalStatusView(h.getId(), h.getNode().getName(), h.getAvailableBeds(), h.getTotalBeds()))
                .collect(Collectors.toList());
        summary.setHospitals(hospitals);

        return summary;
    }

    private Hospital findHospitalById(Long hospitalId) {
        return hospitalRepository.findAllWithNode().stream()
                .filter(h -> h.getId().equals(hospitalId))
                .findFirst()
                .orElse(null);
    }

    private DispatchPlanView persistAndBuildDispatchPlan(PatientIncident incident, OptimizationResult result) {
        DispatchPlan plan = new DispatchPlan();
        plan.setIncident(incident);
        plan.setAlgorithmUsed(result.getAlgorithmUsed());
        plan.setTotalValueAchieved(result.getTotalValueAchieved());
        plan.setCapacityUsed(result.getCapacityUsed());
        plan.setTotalCapacity(result.getTotalCapacity());
        plan = dispatchPlanRepository.save(plan);

        Map<Long, SupplyItem> itemsById = supplyItemRepository.findAll().stream()
                .collect(Collectors.toMap(SupplyItem::getId, si -> si));

        List<String> names = new ArrayList<>();
        for (Long itemId : result.getSelectedItemIds()) {
            SupplyItem item = itemsById.get(itemId);
            if (item != null) {
                DispatchPlanItem planItem = new DispatchPlanItem();
                planItem.setDispatchPlan(plan);
                planItem.setSupplyItem(item);
                dispatchPlanItemRepository.save(planItem);

                names.add(item.getItemName());
                // FIX: mark as LOADED so the next incident's optimization
                // run sees a smaller pending pool - this is what makes
                // supply availability deplete for real.
                item.setStatus(SupplyItemStatus.LOADED);
                supplyItemRepository.save(item);
            }
        }

        DispatchPlanView view = new DispatchPlanView();
        view.setSelectedItemNames(names);
        view.setTotalValueAchieved(result.getTotalValueAchieved());
        view.setCapacityUsed(result.getCapacityUsed());
        view.setTotalCapacity(result.getTotalCapacity());
        view.setAlgorithmUsed(result.getAlgorithmUsed());
        return view;
    }

    private DispatchPlanView emptyDispatchPlan(String reason) {
        DispatchPlanView view = new DispatchPlanView();
        view.setSelectedItemNames(List.of());
        view.setTotalValueAchieved(0);
        view.setCapacityUsed(0);
        view.setTotalCapacity(0);
        view.setNote(reason);
        return view;
    }

    /**
     * Full incident list for the admin panel - includes phone numbers so an
     * admin can verify a report is genuine (e.g. calling back before
     * committing more resources to it). Ordered newest-first.
     */
    public List<IncidentSummaryView> getAllIncidents() {
        return patientIncidentRepository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(i -> new IncidentSummaryView(
                        i.getId(),
                        i.getPatientReference(),
                        i.getPhoneNumber(),
                        i.getConditionType(),
                        i.getSeverityScore(),
                        i.getStatus().name(),
                        i.getLatitude(),
                        i.getLongitude(),
                        i.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
