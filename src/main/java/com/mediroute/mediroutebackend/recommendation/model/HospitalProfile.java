// WHAT: Lightweight in-memory snapshot of a hospital used by the Task 4 recommender.

// WHY: HospitalRecommender must not depend on JPA. This POJO carries only the fields needed to
//      filter, score, and rank: identity, GPS, specialty tags, and bed / ICU counts.

package com.mediroute.mediroutebackend.recommendation.model;

public class HospitalProfile {

    private Long id;
    private String name;
    private String specialty;
    private double latitude;
    private double longitude;
    private int totalBeds;
    private int availableBeds;
    private int totalIcuBeds;
    private int availableIcuBeds;

    public HospitalProfile() {
    }

    public HospitalProfile(Long id, String name, String specialty, double latitude, double longitude,
                           int totalBeds, int availableBeds, int totalIcuBeds, int availableIcuBeds) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.latitude = latitude;
        this.longitude = longitude;
        this.totalBeds = totalBeds;
        this.availableBeds = availableBeds;
        this.totalIcuBeds = totalIcuBeds;
        this.availableIcuBeds = availableIcuBeds;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public int getTotalBeds() { return totalBeds; }
    public void setTotalBeds(int totalBeds) { this.totalBeds = totalBeds; }
    public int getAvailableBeds() { return availableBeds; }
    public void setAvailableBeds(int availableBeds) { this.availableBeds = availableBeds; }
    public int getTotalIcuBeds() { return totalIcuBeds; }
    public void setTotalIcuBeds(int totalIcuBeds) { this.totalIcuBeds = totalIcuBeds; }
    public int getAvailableIcuBeds() { return availableIcuBeds; }
    public void setAvailableIcuBeds(int availableIcuBeds) { this.availableIcuBeds = availableIcuBeds; }
}
