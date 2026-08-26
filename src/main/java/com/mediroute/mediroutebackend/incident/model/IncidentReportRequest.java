package com.mediroute.mediroutebackend.incident.model;

/**
 * The JSON body the dispatcher's "Report Incident" form sends.
 */
public class IncidentReportRequest {

    private String patientReference;
    private String conditionType;
    private int severityScore;
    private double latitude;
    private double longitude;

    public IncidentReportRequest() {}

    public String getPatientReference() { return patientReference; }
    public void setPatientReference(String patientReference) { this.patientReference = patientReference; }
    public String getConditionType() { return conditionType; }
    public void setConditionType(String conditionType) { this.conditionType = conditionType; }
    public int getSeverityScore() { return severityScore; }
    public void setSeverityScore(int severityScore) { this.severityScore = severityScore; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}
