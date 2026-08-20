package com.mediroute.mediroutebackend.allocation.model;

public class AllocationRequest {

    private Long incidentId;
    private String conditionType;
    private int severityScore;
    private int resourceUnitsNeeded;

    public AllocationRequest() {
    }

    public AllocationRequest(Long incidentId, String conditionType, int severityScore, int resourceUnitsNeeded) {
        this.incidentId = incidentId;
        this.conditionType = conditionType;
        this.severityScore = severityScore;
    }

    public Long getIncidentId() { return incidentId; }
    public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }
    public String getConditionType() { return conditionType; }
    public void setConditionType(String conditionType) { this.conditionType = conditionType; }
    public int getSeverityScore() { return severityScore; }
    public void setSeverityScore(int severityScore) { this.severityScore = severityScore; }
    public int getResourceUnitsNeeded() { return resourceUnitsNeeded; }
    public void setResourceUnitsNeeded(int resourceUnitsNeeded) { this.resourceUnitsNeeded = resourceUnitsNeeded; }

    public double densityScore() {
        return (double) severityScore / resourceUnitsNeeded;
    }
}
