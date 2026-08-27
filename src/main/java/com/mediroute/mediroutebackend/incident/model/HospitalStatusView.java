package com.mediroute.mediroutebackend.incident.model;

// This model represents one hospital row in the dashboard table showing current bed usage and capacity.
// It is used by the incident summary to give the control room a quick view of hospital availability.
public class HospitalStatusView {

    private Long hospitalId;
    private String name;
    private int availableBeds;
    private int totalBeds;

    public HospitalStatusView() {}

    public HospitalStatusView(Long hospitalId, String name, int availableBeds, int totalBeds) {
        this.hospitalId = hospitalId;
        this.name = name;
        this.availableBeds = availableBeds;
        this.totalBeds = totalBeds;
    }

    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAvailableBeds() { return availableBeds; }
    public void setAvailableBeds(int availableBeds) { this.availableBeds = availableBeds; }
    public int getTotalBeds() { return totalBeds; }
    public void setTotalBeds(int totalBeds) { this.totalBeds = totalBeds; }
}
