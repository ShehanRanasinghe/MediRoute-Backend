package com.mediroute.mediroutebackend.network.model;

import java.util.List;

/**
 * Output of building the Minimum Spanning Tree - the cheapest possible set
 * of roads that still connects every hospital/depot/junction, representing
 * the "essential backbone" of the referral network.
 *
 */
public class MSTResult {

    private List<MSTEdgeDTO> edges;
    private double totalWeightKm;
    private long executionTimeNanos;
    private String algorithmUsed;
    private boolean connected; // false if the graph was not fully connected

    public MSTResult() {}

    public List<MSTEdgeDTO> getEdges() { return edges; }
    public void setEdges(List<MSTEdgeDTO> edges) { this.edges = edges; }
    public double getTotalWeightKm() { return totalWeightKm; }
    public void setTotalWeightKm(double totalWeightKm) { this.totalWeightKm = totalWeightKm; }
    public long getExecutionTimeNanos() { return executionTimeNanos; }
    public void setExecutionTimeNanos(long executionTimeNanos) { this.executionTimeNanos = executionTimeNanos; }
    public String getAlgorithmUsed() { return algorithmUsed; }
    public void setAlgorithmUsed(String algorithmUsed) { this.algorithmUsed = algorithmUsed; }
    public boolean isConnected() { return connected; }
    public void setConnected(boolean connected) { this.connected = connected; }
}
