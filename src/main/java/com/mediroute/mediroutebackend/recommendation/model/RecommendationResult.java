package com.mediroute.mediroutebackend.recommendation.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Result of a Task 4 hospital recommendation run.
 */
public class RecommendationResult {

    private boolean recommendationFound;
    private Long recommendedHospitalId;
    private String recommendedHospitalName;
    private double recommendedScore;
    private String algorithmUsed;
    private long executionTimeNanos;
    private List<RankedHospital> rankedHospitals = new ArrayList<>();
    private List<RankedHospital> rejectedHospitals = new ArrayList<>();

    public RecommendationResult() {
    }

    public boolean isRecommendationFound() { return recommendationFound; }
    public void setRecommendationFound(boolean recommendationFound) { this.recommendationFound = recommendationFound; }
    public Long getRecommendedHospitalId() { return recommendedHospitalId; }
    public void setRecommendedHospitalId(Long recommendedHospitalId) { this.recommendedHospitalId = recommendedHospitalId; }
    public String getRecommendedHospitalName() { return recommendedHospitalName; }
    public void setRecommendedHospitalName(String recommendedHospitalName) { this.recommendedHospitalName = recommendedHospitalName; }
    public double getRecommendedScore() { return recommendedScore; }
    public void setRecommendedScore(double recommendedScore) { this.recommendedScore = recommendedScore; }
    public String getAlgorithmUsed() { return algorithmUsed; }
    public void setAlgorithmUsed(String algorithmUsed) { this.algorithmUsed = algorithmUsed; }
    public long getExecutionTimeNanos() { return executionTimeNanos; }
    public void setExecutionTimeNanos(long executionTimeNanos) { this.executionTimeNanos = executionTimeNanos; }
    public List<RankedHospital> getRankedHospitals() { return rankedHospitals; }
    public void setRankedHospitals(List<RankedHospital> rankedHospitals) { this.rankedHospitals = rankedHospitals; }
    public List<RankedHospital> getRejectedHospitals() { return rejectedHospitals; }
    public void setRejectedHospitals(List<RankedHospital> rejectedHospitals) { this.rejectedHospitals = rejectedHospitals; }
}
