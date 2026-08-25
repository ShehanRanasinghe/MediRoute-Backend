package com.mediroute.mediroutebackend.decision.model;

/**
 * The JSON body the frontend sends to request a hospital recommendation.
 * Example: { "conditionType": "CARDIAC", "patientLatitude": 6.93,
 *            "patientLongitude": 79.86, "topK": 3 }
 */
public class RecommendationRequest {

    private String conditionType;
    private double patientLatitude;
    private double patientLongitude;
    private Integer topK; // defaults to 3 if not provided

    public RecommendationRequest() {}

    public String getConditionType() { return conditionType; }
    public void setConditionType(String conditionType) { this.conditionType = conditionType; }
    public double getPatientLatitude() { return patientLatitude; }
    public void setPatientLatitude(double patientLatitude) { this.patientLatitude = patientLatitude; }
    public double getPatientLongitude() { return patientLongitude; }
    public void setPatientLongitude(double patientLongitude) { this.patientLongitude = patientLongitude; }
    public Integer getTopK() { return topK; }
    public void setTopK(Integer topK) { this.topK = topK; }
}
