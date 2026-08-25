package com.mediroute.mediroutebackend.decision.model;

/**
 * One scored candidate hospital - both the internal "item to rank" and the
 * final output shape shown to the user, so no separate mapping step is
 * needed between scoring and display.
 */
public class HospitalRecommendation {

    private Long hospitalId;
    private String hospitalName;
    private double score;
    private double distanceKm;
    private boolean specialtyMatch;
    private int availableBeds;

    public HospitalRecommendation() {}

    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }
    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }
    public boolean isSpecialtyMatch() { return specialtyMatch; }
    public void setSpecialtyMatch(boolean specialtyMatch) { this.specialtyMatch = specialtyMatch; }
    public int getAvailableBeds() { return availableBeds; }
    public void setAvailableBeds(int availableBeds) { this.availableBeds = availableBeds; }
}
