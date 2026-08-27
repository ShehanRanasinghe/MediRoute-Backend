package com.mediroute.mediroutebackend.common.models;

// This enum tracks the lifecycle of an incident from first report to completion.
// Each status shows the current stage of the response and helps the system decide which next action to take.
// It is used across services to keep the incident state consistent throughout dispatch and follow-up.
public enum IncidentStatus {
    /** The incident is awaiting resource and hospital allocation. */
    PENDING,
    /** A response resource and destination have been allocated. */
    ASSIGNED,
    /** The assigned response resource is travelling to the incident. */
    EN_ROUTE,
    /** The incident response has finished. */
    COMPLETED
}
