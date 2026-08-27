package com.mediroute.mediroutebackend.allocation.model;

// This DTO carries the type of resource being allocated and the strategy the client wants to use.
// The controller reads the JSON payload and passes these values into the allocation service.
public class AllocationRequestDTO {

    // This tells the backend which resource pool is being considered, such as ambulance or ICU bed.
    private String resourceType; // AMBULANCE | ICU_BED | WARD_BED | VENTILATOR
    // This selects the comparison strategy, with greedy as the default and knapsack as the optimization option.
    private String algorithm;    // "greedy" (default) or "knapsack"

    public AllocationRequestDTO() {
    }

    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
}
