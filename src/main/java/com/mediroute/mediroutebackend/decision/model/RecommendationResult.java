package com.mediroute.mediroutebackend.decision.model;

import java.util.List;

/**
 * Output of running a top-k selection algorithm (heap-based or full-sort).
 * Both algorithms return this same shape so they can be compared directly.
 */
public class RecommendationResult {

    private List<HospitalRecommendation> rankedHospitals;
    private long executionTimeNanos;
    private String algorithmUsed;

    public RecommendationResult() {}

    public List<HospitalRecommendation> getRankedHospitals() { return rankedHospitals; }
    public void setRankedHospitals(List<HospitalRecommendation> rankedHospitals) { this.rankedHospitals = rankedHospitals; }
    public long getExecutionTimeNanos() { return executionTimeNanos; }
    public void setExecutionTimeNanos(long executionTimeNanos) { this.executionTimeNanos = executionTimeNanos; }
    public String getAlgorithmUsed() { return algorithmUsed; }
    public void setAlgorithmUsed(String algorithmUsed) { this.algorithmUsed = algorithmUsed; }
}
