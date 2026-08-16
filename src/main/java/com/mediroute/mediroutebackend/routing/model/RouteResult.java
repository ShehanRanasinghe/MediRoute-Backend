package com.mediroute.mediroutebackend.routing.model;

import java.util.List;

/**
 * Data Transfer Object (DTO) holding the output and performance metrics
 * of a routing algorithm run (Dijkstra or A*).
 */
public class RouteResult {

    private List<Long> path; // Ordered list of node IDs from source to destination
    private double totalDistanceKm; // Cumulative distance of the calculated route in kilometers
    private double totalTimeMinutes; // Estimated travel time in minutes based on road speeds
    private long executionTimeNanos; // Algorithm execution time measured in nanoseconds
    private String algorithmUsed; // Name of the algorithm applied ("Dijkstra" or "A*")
    private boolean pathFound; // Flag indicating whether a valid path was successfully resolved

    /**
     * Default constructor required for JSON serialization (Jackson/Spring).
     */
    public RouteResult() {
    }

    // Getters and Setters

    public List<Long> getPath() {
        return path;
    }

    public void setPath(List<Long> path) {
        this.path = path;
    }

    public double getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public void setTotalDistanceKm(double totalDistanceKm) {
        this.totalDistanceKm = totalDistanceKm;
    }

    public double getTotalTimeMinutes() {
        return totalTimeMinutes;
    }

    public void setTotalTimeMinutes(double totalTimeMinutes) {
        this.totalTimeMinutes = totalTimeMinutes;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    public void setExecutionTimeNanos(long executionTimeNanos) {
        this.executionTimeNanos = executionTimeNanos;
    }

    public String getAlgorithmUsed() {
        return algorithmUsed;
    }

    public void setAlgorithmUsed(String algorithmUsed) {
        this.algorithmUsed = algorithmUsed;
    }

    public boolean isPathFound() {
        return pathFound;
    }

    public void setPathFound(boolean pathFound) {
        this.pathFound = pathFound;
    }
}