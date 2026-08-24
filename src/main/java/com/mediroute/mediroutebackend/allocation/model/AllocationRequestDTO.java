package com.mediroute.mediroutebackend.allocation.model;


public class AllocationRequestDTO {

    private String resourceType; // AMBULANCE | ICU_BED | WARD_BED | VENTILATOR
    private String algorithm;    // "greedy" (default) or "knapsack"

    public AllocationRequestDTO() {
    }

    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
}
