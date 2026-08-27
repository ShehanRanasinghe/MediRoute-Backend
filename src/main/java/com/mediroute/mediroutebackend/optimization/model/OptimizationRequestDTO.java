package com.mediroute.mediroutebackend.optimization.model;


// This request model captures the vehicle capacity and the algorithm selection for the optimization run.
// It is sent by the frontend when a dispatcher wants a loadout recommendation or comparison.
public class OptimizationRequestDTO {

    private int vehicleCapacity;
    private String algorithm; // "dp" (default) | "greedy" | "backtracking"

    public OptimizationRequestDTO() {}

    public int getVehicleCapacity() { return vehicleCapacity; }
    public void setVehicleCapacity(int vehicleCapacity) { this.vehicleCapacity = vehicleCapacity; }
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
}
