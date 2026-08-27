package com.mediroute.mediroutebackend.allocation.service;

import com.mediroute.mediroutebackend.allocation.algorithm.GreedyAllocator;
import com.mediroute.mediroutebackend.allocation.algorithm.KnapsackAllocator;
import com.mediroute.mediroutebackend.allocation.model.AllocationRequest;
import com.mediroute.mediroutebackend.allocation.model.AllocationResult;
import com.mediroute.mediroutebackend.common.models.IncidentStatus;
import com.mediroute.mediroutebackend.common.models.PatientIncident;
import com.mediroute.mediroutebackend.common.models.ResourceStatus;
import com.mediroute.mediroutebackend.common.models.ResourceType;
import com.mediroute.mediroutebackend.common.models.repository.PatientIncidentRepository;
import com.mediroute.mediroutebackend.common.models.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


// This service connects the pending incident list with the available resources and decides which allocator should run.
// It acts as the controller layer between the database records and the greedy or knapsack selection logic.
@Service
public class AllocationService {

    // The repository gives access to current pending incidents that still need a resource assignment.
    @Autowired
    private PatientIncidentRepository incidentRepository;

    // This repository keeps the live resource inventory for each type such as ambulance or ICU bed.
    @Autowired
    private ResourceRepository resourceRepository;

    // These allocator implementations are reused so the service can run either strategy with the same input data.
    private final GreedyAllocator greedyAllocator = new GreedyAllocator();
    private final KnapsackAllocator knapsackAllocator = new KnapsackAllocator();

    // This method picks the active algorithm and returns the allocation result for the chosen resource category.
    public AllocationResult runAllocation(ResourceType resourceType, String algorithm) {
        List<AllocationRequest> requests = loadPendingRequests();
        int capacity = countAvailableResources(resourceType);

        if (algorithm != null && algorithm.equalsIgnoreCase("knapsack")) {
            return knapsackAllocator.allocate(requests, capacity);
        }
        return greedyAllocator.allocate(requests, capacity);
    }

    // This method runs both algorithms side by side so the system can compare their result quality and speed.
    public Map<String, AllocationResult> compareAlgorithms(ResourceType resourceType) {
        List<AllocationRequest> requests = loadPendingRequests();
        int capacity = countAvailableResources(resourceType);

        Map<String, AllocationResult> results = new HashMap<>();
        results.put("greedy", greedyAllocator.allocate(requests, capacity));
        results.put("knapsack", knapsackAllocator.allocate(requests, capacity));
        return results;
    }

    // This exposes the unresolved incidents so other parts of the system can inspect the current backlog.
    public List<PatientIncident> getPendingIncidents() {
        return incidentRepository.findByStatus(IncidentStatus.PENDING);
    }

    // Each pending incident is converted into a lightweight allocation request with a required resource cost.
    private List<AllocationRequest> loadPendingRequests() {
        return incidentRepository.findByStatus(IncidentStatus.PENDING).stream()
                .map(incident -> new AllocationRequest(
                        incident.getId(),
                        incident.getConditionType(),
                        incident.getSeverityScore(),
                        // ASSUMPTION: critical incidents (severity >= 8) need 2 resource
                        // units; standard incidents need 1. See docs/01-problem-analysis.md
                        incident.getSeverityScore() >= 8 ? 2 : 1
                ))
                .collect(Collectors.toList());
    }

    // This counts only resources that are currently available, so the algorithm does not over-allocate from a locked inventory.
    private int countAvailableResources(ResourceType resourceType) {
        return resourceRepository.findByResourceTypeAndStatus(resourceType, ResourceStatus.AVAILABLE).size();
    }
}
