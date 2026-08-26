package com.mediroute.mediroutebackend.incident.model;

import com.mediroute.mediroutebackend.decision.model.HospitalRecommendation;
import com.mediroute.mediroutebackend.routing.model.RouteResult;

/**
 * The single combined result returned after reporting one incident -
 * everything the dispatcher sees on screen after clicking "Dispatch
 * Response". This is the actual integration output: one object built from
 * calling all 5 modules' existing services in sequence.
 */
public class IncidentResponse {

    private Long incidentId;
    private HospitalRecommendation recommendedHospital; // from the Decision module
    private RouteResult route;                          // from the Routing module
    private boolean ambulanceAllocated;                  // from the Allocation module
    private DispatchPlanView dispatchPlan;               // from the Optimization module
    private boolean routeUsesCriticalNode;               // from the Network Analysis module
    private long overallProcessingTimeNanos;

    public IncidentResponse() {}

    public Long getIncidentId() { return incidentId; }
    public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }
    public HospitalRecommendation getRecommendedHospital() { return recommendedHospital; }
    public void setRecommendedHospital(HospitalRecommendation recommendedHospital) { this.recommendedHospital = recommendedHospital; }
    public RouteResult getRoute() { return route; }
    public void setRoute(RouteResult route) { this.route = route; }
    public boolean isAmbulanceAllocated() { return ambulanceAllocated; }
    public void setAmbulanceAllocated(boolean ambulanceAllocated) { this.ambulanceAllocated = ambulanceAllocated; }
    public DispatchPlanView getDispatchPlan() { return dispatchPlan; }
    public void setDispatchPlan(DispatchPlanView dispatchPlan) { this.dispatchPlan = dispatchPlan; }
    public boolean isRouteUsesCriticalNode() { return routeUsesCriticalNode; }
    public void setRouteUsesCriticalNode(boolean routeUsesCriticalNode) { this.routeUsesCriticalNode = routeUsesCriticalNode; }
    public long getOverallProcessingTimeNanos() { return overallProcessingTimeNanos; }
    public void setOverallProcessingTimeNanos(long overallProcessingTimeNanos) { this.overallProcessingTimeNanos = overallProcessingTimeNanos; }
}
