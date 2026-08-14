package com.mediroute.routing.model;

import java.util.List;

/**
 * The output of a routing algorithm run (Dijkstra or A*).
 *
 * 
 */
public class RouteResult {

    private List<Long> path;
    private double totalDistanceKm;
    private double totalTimeMinutes;
    private long executionTimeNanos;
    private String algorithmUsed;
    private boolean pathFound;

    public RouteResult() {
    }

    public List<Long> getPath() { return path; }
    public void setPath(List<Long> path) { this.path = path; }
    public double getTotalDistanceKm() { return totalDistanceKm; }
    public void setTotalDistanceKm(double totalDistanceKm) { this.totalDistanceKm = totalDistanceKm; }
    public double getTotalTimeMinutes() { return totalTimeMinutes; }
    public void setTotalTimeMinutes(double totalTimeMinutes) { this.totalTimeMinutes = totalTimeMinutes; }
    public long getExecutionTimeNanos() { return executionTimeNanos; }
    public void setExecutionTimeNanos(long executionTimeNanos) { this.executionTimeNanos = executionTimeNanos; }
    public String getAlgorithmUsed() { return algorithmUsed; }
    public void setAlgorithmUsed(String algorithmUsed) { this.algorithmUsed = algorithmUsed; }
    public boolean isPathFound() { return pathFound; }
    public void setPathFound(boolean pathFound) { this.pathFound = pathFound; }
}
