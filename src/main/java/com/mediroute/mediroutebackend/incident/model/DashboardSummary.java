package com.mediroute.mediroutebackend.incident.model;

import java.util.List;

/**
 * System-wide status shown on the Control Room dashboard - pulls a light
 * summary from each module (not full algorithm runs) so the page loads
 * fast.
 */
public class DashboardSummary {

    private int pendingIncidents;
    private int ongoingIncidents;
    private int availableAmbulances;
    private int criticalNodeCount;
    private List<HospitalStatusView> hospitals;

    public DashboardSummary() {}

    public int getPendingIncidents() { return pendingIncidents; }
    public void setPendingIncidents(int pendingIncidents) { this.pendingIncidents = pendingIncidents; }
    public int getOngoingIncidents() { return ongoingIncidents; }
    public void setOngoingIncidents(int ongoingIncidents) { this.ongoingIncidents = ongoingIncidents; }
    public int getAvailableAmbulances() { return availableAmbulances; }
    public void setAvailableAmbulances(int availableAmbulances) { this.availableAmbulances = availableAmbulances; }
    public int getCriticalNodeCount() { return criticalNodeCount; }
    public void setCriticalNodeCount(int criticalNodeCount) { this.criticalNodeCount = criticalNodeCount; }
    public List<HospitalStatusView> getHospitals() { return hospitals; }
    public void setHospitals(List<HospitalStatusView> hospitals) { this.hospitals = hospitals; }
}
