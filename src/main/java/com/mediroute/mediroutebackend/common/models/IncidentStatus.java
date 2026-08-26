package com.mediroute.mediroutebackend.common.models;

/**
 * What: Lists the possible states of a patient incident.
 * Why: A clear status is needed to track dispatch progress.
 * How: The incident moves through these values from creation to completion.
 */
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
