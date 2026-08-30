package com.mediroute.mediroutebackend.incident.model;

import java.time.LocalDateTime;

/**
 * One row in the admin panel's incident list - includes the phone number
 * so an admin can verify a report is genuine before it's acted on further.
 *
 * SECURITY NOTE: this endpoint is not protected at the Spring Boot API
 * level - the admin login only gates the frontend page, not this URL
 * itself. Anyone who knows the URL could call it directly. Accepted as a
 * documented limitation for this coursework project, the same way the
 * rest of the app has no server-side authentication - see the README.
 */
public class IncidentSummaryView {

    private Long id;
    private String patientReference;
    private String phoneNumber;
    private String conditionType;
    private int severityScore;
    private String status;
    private double latitude;
    private double longitude;
    private LocalDateTime createdAt;

    public IncidentSummaryView() {}

    public IncidentSummaryView(Long id, String patientReference, String phoneNumber, String conditionType,
                                int severityScore, String status, double latitude, double longitude,
                                LocalDateTime createdAt) {
        this.id = id;
        this.patientReference = patientReference;
        this.phoneNumber = phoneNumber;
        this.conditionType = conditionType;
        this.severityScore = severityScore;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPatientReference() { return patientReference; }
    public void setPatientReference(String patientReference) { this.patientReference = patientReference; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getConditionType() { return conditionType; }
    public void setConditionType(String conditionType) { this.conditionType = conditionType; }
    public int getSeverityScore() { return severityScore; }
    public void setSeverityScore(int severityScore) { this.severityScore = severityScore; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
