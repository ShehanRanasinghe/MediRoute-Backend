package com.mediroute.routing.model;

/**
 * Data Transfer Object (DTO) representing the JSON payload sent by
 * the frontend to request a route between two network nodes.
 */
public class RouteRequest {

    private Long sourceId;
    private Long destinationId;
    private String algorithm; // Specifies algorithm preference: "dijkstra" or "astar"

    /**
     * Default constructor required for JSON deserialization (Jackson/Spring).
     */
    public RouteRequest() {
    }

    // Getters and Setters

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}