package com.mediroute.mediroutebackend.recommendation.model;

/**
 * One hospital after Task 4 scoring, including the breakdown a dispatcher can read.
 */
public class RankedHospital {

    private Long hospitalId;
    private String hospitalName;
    private String specialty;
    private double distanceKm;
    private double specialtyScore;
    private double distanceScore;
    private double availabilityScore;
    private double totalScore;
    private String rejectionReason;

    public RankedHospital() {
    }

    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }
    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }
    public double getSpecialtyScore() { return specialtyScore; }
    public void setSpecialtyScore(double specialtyScore) { this.specialtyScore = specialtyScore; }
    public double getDistanceScore() { return distanceScore; }
    public void setDistanceScore(double distanceScore) { this.distanceScore = distanceScore; }
    public double getAvailabilityScore() { return availabilityScore; }
    public void setAvailabilityScore(double availabilityScore) { this.availabilityScore = availabilityScore; }
    public double getTotalScore() { return totalScore; }
    public void setTotalScore(double totalScore) { this.totalScore = totalScore; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}
