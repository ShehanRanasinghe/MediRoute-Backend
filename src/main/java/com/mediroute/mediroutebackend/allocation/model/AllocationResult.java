package com.mediroute.mediroutebackend.allocation.model;

import java.util.List;

/**
 * Contains the decisions and performance details produced by an allocation algorithm.
 * A common result type makes different allocation strategies easy to compare.
 */
public class AllocationResult {

    private List<Long> selectedIncidentIds;
    private List<Long> unallocatedIncidentIds;
    private int totalValueAchieved;
    private int capacityUsed;
    private int totalCapacity;
    private long executionTimeNanos;
    private String algorithmUsed;

    /** Creates an empty result so an allocation service can populate it progressively. */
    public AllocationResult() {
    }

    /** Returns the IDs of incidents that received resources. */
    public List<Long> getSelectedIncidentIds() {
        return selectedIncidentIds;
    }

    /** Stores the IDs of incidents selected to receive resources. */
    public void setSelectedIncidentIds(List<Long> selectedIncidentIds) {
        this.selectedIncidentIds = selectedIncidentIds;
    }

    /** Returns the IDs of incidents that could not be allocated resources. */
    public List<Long> getUnallocatedIncidentIds() {
        return unallocatedIncidentIds;
    }

    /** Stores the IDs of incidents left without resources in this allocation run. */
    public void setUnallocatedIncidentIds(List<Long> unallocatedIncidentIds) {
        this.unallocatedIncidentIds = unallocatedIncidentIds;
    }

    /** Returns the combined severity score of all selected incidents. */
    public int getTotalValueAchieved() {
        return totalValueAchieved;
    }

    /** Stores the combined severity score achieved by the allocation. */
    public void setTotalValueAchieved(int totalValueAchieved) {
        this.totalValueAchieved = totalValueAchieved;
    }

    /** Returns the number of resource units consumed by selected incidents. */
    public int getCapacityUsed() {
        return capacityUsed;
    }

    /** Stores the number of resource units consumed by the allocation. */
    public void setCapacityUsed(int capacityUsed) {
        this.capacityUsed = capacityUsed;
    }

    /** Returns the maximum resource capacity available for the allocation run. */
    public int getTotalCapacity() {
        return totalCapacity;
    }

    /** Stores the maximum resource capacity available to the algorithm. */
    public void setTotalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    /** Returns how long the allocation algorithm took, measured in nanoseconds. */
    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    /** Stores the algorithm's execution duration in nanoseconds for comparison. */
    public void setExecutionTimeNanos(long executionTimeNanos) {
        this.executionTimeNanos = executionTimeNanos;
    }

    /** Returns the name of the allocation algorithm that produced this result. */
    public String getAlgorithmUsed() {
        return algorithmUsed;
    }

    /** Stores the name of the allocation algorithm for result traceability. */
    public void setAlgorithmUsed(String algorithmUsed) {
        this.algorithmUsed = algorithmUsed;
    }
}
