package com.mediroute.mediroutebackend.optimization.model;


public class OptimizationRequestDTO {

    private int vehicleCapacity;
    private String algorithm; // "dp" (default) | "greedy" | "backtracking"

    public OptimizationRequestDTO() {}

    public int getVehicleCapacity() { return vehicleCapacity; }
    public void setVehicleCapacity(int vehicleCapacity) { this.vehicleCapacity = vehicleCapacity; }
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
}
