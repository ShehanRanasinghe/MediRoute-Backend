package com.mediroute.mediroutebackend.common.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;


/**
 * What: Represents a reported patient emergency.
 * Why: The dispatch system needs the incident details to plan a response.
 * How: It stores the location, severity, status, and assigned response details.
 */
@Entity
@Table(name = "patient_incident")
public class PatientIncident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** External reference used to identify the patient without storing clinical details. */
    @Column(name = "patient_reference", length = 100)
    private String patientReference;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(name = "condition_type", length = 50)
    private String conditionType;

    /** Triage score used when prioritizing incidents for dispatch. */
    @Column(name = "severity_score", nullable = false)
    private int severityScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IncidentStatus status = IncidentStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_hospital_id")
    private Hospital assignedHospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_resource_id")
    private Resource assignedResource;

    /** Time at which the incident record was created. */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public PatientIncident() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPatientReference() { return patientReference; }
    public void setPatientReference(String patientReference) { this.patientReference = patientReference; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public String getConditionType() { return conditionType; }
    public void setConditionType(String conditionType) { this.conditionType = conditionType; }
    public int getSeverityScore() { return severityScore; }
    public void setSeverityScore(int severityScore) { this.severityScore = severityScore; }
    public IncidentStatus getStatus() { return status; }
    public void setStatus(IncidentStatus status) { this.status = status; }
    public Hospital getAssignedHospital() { return assignedHospital; }
    public void setAssignedHospital(Hospital assignedHospital) { this.assignedHospital = assignedHospital; }
    public Resource getAssignedResource() { return assignedResource; }
    public void setAssignedResource(Resource assignedResource) { this.assignedResource = assignedResource; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
