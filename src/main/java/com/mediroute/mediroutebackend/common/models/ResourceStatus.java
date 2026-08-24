package com.mediroute.mediroutebackend.common.models;

/**
 * What: Lists the operational states of a medical resource.
 * Why: Dispatchers must know whether a resource is ready for use.
 * How: The current value controls whether the resource can be assigned.
 */
public enum ResourceStatus {
    /** Ready to be assigned to an incident. */
    AVAILABLE,
    /** Currently assigned or otherwise operationally occupied. */
    IN_USE,
    /** Temporarily unavailable while undergoing maintenance. */
    MAINTENANCE
}
