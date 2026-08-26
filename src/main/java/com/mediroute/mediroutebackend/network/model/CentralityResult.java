package com.mediroute.network.model;

import java.util.List;

/**
 * Output of ranking every node by degree centrality (how many roads
 * connect directly to it) - used for regional referral planning (which
 * hospitals are the most "connected" hubs).
 *
 */
public class CentralityResult {

    private List<NodeCentrality> rankedNodes; // sorted highest degree first
    private long executionTimeNanos;
    private String algorithmUsed;

    public CentralityResult() {}

    public List<NodeCentrality> getRankedNodes() { return rankedNodes; }
    public void setRankedNodes(List<NodeCentrality> rankedNodes) { this.rankedNodes = rankedNodes; }
    public long getExecutionTimeNanos() { return executionTimeNanos; }
    public void setExecutionTimeNanos(long executionTimeNanos) { this.executionTimeNanos = executionTimeNanos; }
    public String getAlgorithmUsed() { return algorithmUsed; }
    public void setAlgorithmUsed(String algorithmUsed) { this.algorithmUsed = algorithmUsed; }
}
