package com.mediroute.mediroutebackend.common.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;


// This entity represents a reported medical emergency that needs a response plan.
// It keeps the patient location, condition type, severity score, and current dispatch state.
// The incident record connects the decision, allocation, and routing services so they can process one emergency together.
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

    /** Creates an empty incident entity with its default status and creation time. */
    public PatientIncident() {
    }

    /** Returns the database identifier of this patient incident. */
    public Long getId() {
        return id;
    }

    /** Updates the database identifier of this patient incident. */
    public void setId(Long id) {
        this.id = id;
    }

    /** Returns the external reference used to identify the patient. */
    public String getPatientReference() {
        return patientReference;
    }

    /** Updates the external reference used to identify the patient. */
    public void setPatientReference(String patientReference) {
        this.patientReference = patientReference;
    }

    /** Returns the latitude of the reported incident location. */
    public double getLatitude() {
        return latitude;
    }

    /** Updates the latitude of the reported incident location. */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /** Returns the longitude of the reported incident location. */
    public double getLongitude() {
        return longitude;
    }

    /** Updates the longitude of the reported incident location. */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /** Returns the type of medical condition reported for the patient. */
    public String getConditionType() {
        return conditionType;
    }

    /** Updates the type of medical condition reported for the patient. */
    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    /** Returns the triage severity score used to prioritize this incident. */
    public int getSeverityScore() {
        return severityScore;
    }

    /** Updates the triage severity score used to prioritize this incident. */
    public void setSeverityScore(int severityScore) {
        this.severityScore = severityScore;
    }

    /** Returns the current dispatch status of this incident. */
    public IncidentStatus getStatus() {
        return status;
    }

    /** Updates the current dispatch status of this incident. */
    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    /** Returns the hospital selected to receive the patient. */
    public Hospital getAssignedHospital() {
        return assignedHospital;
    }

    /** Assigns the hospital selected to receive the patient. */
    public void setAssignedHospital(Hospital assignedHospital) {
        this.assignedHospital = assignedHospital;
    }

    /** Returns the medical resource assigned to this incident. */
    public Resource getAssignedResource() {
        return assignedResource;
    }

    /** Assigns the medical resource responsible for this incident. */
    public void setAssignedResource(Resource assignedResource) {
        this.assignedResource = assignedResource;
    }

    /** Returns the date and time when this incident was recorded. */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** Updates the date and time when this incident was recorded. */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
