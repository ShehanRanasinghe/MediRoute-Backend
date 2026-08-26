package com.mediroute.mediroutebackend.network.model;

import java.util.List;

/**
 * Output of the articulation point (critical node) detection.
 * A critical node is one whose removal would disconnect part of the
 * hospital/road network - e.g. a junction that is the ONLY route to a
 * hospital.
 */
public class CriticalNodeResult {

    private List<Long> criticalNodeIds;
    private long executionTimeNanos;
    private String algorithmUsed;

    public CriticalNodeResult() {}

    public List<Long> getCriticalNodeIds() { return criticalNodeIds; }
    public void setCriticalNodeIds(List<Long> criticalNodeIds) { this.criticalNodeIds = criticalNodeIds; }
    public long getExecutionTimeNanos() { return executionTimeNanos; }
    public void setExecutionTimeNanos(long executionTimeNanos) { this.executionTimeNanos = executionTimeNanos; }
    public String getAlgorithmUsed() { return algorithmUsed; }
    public void setAlgorithmUsed(String algorithmUsed) { this.algorithmUsed = algorithmUsed; }
}
