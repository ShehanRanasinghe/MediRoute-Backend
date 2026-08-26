package com.mediroute.mediroutebackend.network.model;

public class MSTEdgeDTO {

    private Long fromNodeId;
    private Long toNodeId;
    private double weightKm;

    public MSTEdgeDTO() {}

    public MSTEdgeDTO(Long fromNodeId, Long toNodeId, double weightKm) {
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
        this.weightKm = weightKm;
    }

    public Long getFromNodeId() { return fromNodeId; }
    public void setFromNodeId(Long fromNodeId) { this.fromNodeId = fromNodeId; }
    public Long getToNodeId() { return toNodeId; }
    public void setToNodeId(Long toNodeId) { this.toNodeId = toNodeId; }
    public double getWeightKm() { return weightKm; }
    public void setWeightKm(double weightKm) { this.weightKm = weightKm; }
}
