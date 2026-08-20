package com.mediroute.mediroutebackend.allocation.model;

/**
 * Represents an incident and the resources it needs during allocation.
 * Allocation algorithms use its severity and resource cost to prioritize incidents.
 */
public class AllocationRequest {

    private Long incidentId;
    private String conditionType;
    private int severityScore;
    private int resourceUnitsNeeded;

    /** Creates an empty request so its values can be assigned later. */
    public AllocationRequest() {
    }

    /** Creates a request with all incident and resource details. */
    public AllocationRequest(Long incidentId, String conditionType, int severityScore, int resourceUnitsNeeded) {
        this.incidentId = incidentId;
        this.conditionType = conditionType;
        this.severityScore = severityScore;
        this.resourceUnitsNeeded = resourceUnitsNeeded;
    }

    /** Returns the ID used to identify the incident during allocation. */
    public Long getIncidentId() {
        return incidentId;
    }

    /** Assigns the incident that this allocation request represents. */
    public void setIncidentId(Long incidentId) {
        this.incidentId = incidentId;
    }

    /** Returns the patient's reported condition type. */
    public String getConditionType() {
        return conditionType;
    }

    /** Assigns the patient's reported condition type. */
    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    /** Returns the urgency score used to prioritize the incident. */
    public int getSeverityScore() {
        return severityScore;
    }

    /** Sets the urgency score used by the allocation algorithm. */
    public void setSeverityScore(int severityScore) {
        this.severityScore = severityScore;
    }

    /** Returns the number of resource units needed to serve the incident. */
    public int getResourceUnitsNeeded() {
        return resourceUnitsNeeded;
    }

    /** Sets how many units of the available resource the incident requires. */
    public void setResourceUnitsNeeded(int resourceUnitsNeeded) {
        this.resourceUnitsNeeded = resourceUnitsNeeded;
    }

    /**
     * Calculates urgency per resource unit so a greedy algorithm can rank requests.
     *
     * @return the severity score divided by the required resource units
     */
    public double densityScore() {
        return (double) severityScore / resourceUnitsNeeded;
    }
}
