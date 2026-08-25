package com.mediroute.mediroutebackend.optimization.model;

import java.util.List;

public class OptimizationResult {

    private List<Long> selectedItemIds;
    private List<Long> unselectedItemIds;
    private int totalValueAchieved;
    private int capacityUsed;
    private int totalCapacity;
    private long executionTimeNanos;
    private String algorithmUsed;

    public OptimizationResult() {}

    public List<Long> getSelectedItemIds() { return selectedItemIds; }
    public void setSelectedItemIds(List<Long> selectedItemIds) { this.selectedItemIds = selectedItemIds; }
    public List<Long> getUnselectedItemIds() { return unselectedItemIds; }
    public void setUnselectedItemIds(List<Long> unselectedItemIds) { this.unselectedItemIds = unselectedItemIds; }
    public int getTotalValueAchieved() { return totalValueAchieved; }
    public void setTotalValueAchieved(int totalValueAchieved) { this.totalValueAchieved = totalValueAchieved; }
    public int getCapacityUsed() { return capacityUsed; }
    public void setCapacityUsed(int capacityUsed) { this.capacityUsed = capacityUsed; }
    public int getTotalCapacity() { return totalCapacity; }
    public void setTotalCapacity(int totalCapacity) { this.totalCapacity = totalCapacity; }
    public long getExecutionTimeNanos() { return executionTimeNanos; }
    public void setExecutionTimeNanos(long executionTimeNanos) { this.executionTimeNanos = executionTimeNanos; }
    public String getAlgorithmUsed() { return algorithmUsed; }
    public void setAlgorithmUsed(String algorithmUsed) { this.algorithmUsed = algorithmUsed; }
}
