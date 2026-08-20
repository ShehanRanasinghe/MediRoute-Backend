package com.mediroute.mediroutebackend.allocation.model;
import java.util.List;

public class AllocationResult {

    private List<Long> selectedIncidentIds;
    private List<Long> unallocatedIncidentIds;
    private int totalValueAchieved;
    private int capacityUsed;
    private int totalCapacity;
    private long executionTimeNanos;
    private String algorithmUsed;

    public AllocationResult() {
    }

    public List<Long> getSelectedIncidentIds() { return selectedIncidentIds; }
    public void setSelectedIncidentIds(List<Long> selectedIncidentIds) { this.selectedIncidentIds = selectedIncidentIds; }
    public List<Long> getUnallocatedIncidentIds() { return unallocatedIncidentIds; }
    public void setUnallocatedIncidentIds(List<Long> unallocatedIncidentIds) { this.unallocatedIncidentIds = unallocatedIncidentIds; }
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
