package com.mediroute.routing.model;

/**
 * The JSON body the frontend sends to request a route.

 */
public class RouteRequest {

    private Long sourceId;
    private Long destinationId;
    private String algorithm; // "dijkstra" (default) or "astar"

    public RouteRequest() {
    }

    public Long getSourceId() { return sourceId; }
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
    public Long getDestinationId() { return destinationId; }
    public void setDestinationId(Long destinationId) { this.destinationId = destinationId; }
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
}
