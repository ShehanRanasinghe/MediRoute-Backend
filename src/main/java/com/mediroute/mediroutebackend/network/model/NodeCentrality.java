package com.mediroute.network.model;

/** One node's degree centrality score. */
public class NodeCentrality {

    private Long nodeId;
    private String name;
    private int degreeScore; // number of direct road connections

    public NodeCentrality() {}

    public NodeCentrality(Long nodeId, String name, int degreeScore) {
        this.nodeId = nodeId;
        this.name = name;
        this.degreeScore = degreeScore;
    }

    public Long getNodeId() { return nodeId; }
    public void setNodeId(Long nodeId) { this.nodeId = nodeId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getDegreeScore() { return degreeScore; }
    public void setDegreeScore(int degreeScore) { this.degreeScore = degreeScore; }
}
