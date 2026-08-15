package com.mediroute.mediroutebackend.recommendation.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * JSON body for POST /api/recommendations.
 * Describes one patient incident that needs a hospital recommendation.
 */
public class RecommendationRequest {

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double latitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double longitude;

    @NotBlank
    private String conditionType;

    @NotNull
    @Min(1)
    @Max(10)
    private Integer severityScore;

    public RecommendationRequest() {
    }

    public RecommendationRequest(Double latitude, Double longitude, String conditionType, Integer severityScore) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.conditionType = conditionType;
        this.severityScore = severityScore;
    }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getConditionType() { return conditionType; }
    public void setConditionType(String conditionType) { this.conditionType = conditionType; }
    public Integer getSeverityScore() { return severityScore; }
    public void setSeverityScore(Integer severityScore) { this.severityScore = severityScore; }
}
