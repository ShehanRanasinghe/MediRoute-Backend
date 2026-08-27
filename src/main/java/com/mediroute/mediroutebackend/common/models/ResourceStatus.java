package com.mediroute.mediroutebackend.common.models;

// This enum shows whether a medical resource is ready to use or currently unavailable.
// The system checks this value before assigning a resource so it does not send an item that is already occupied or broken.
// It helps keep the dispatch process realistic and safe.
public enum ResourceStatus {
    /** Ready to be assigned to an incident. */
    AVAILABLE,
    /** Currently assigned or otherwise operationally occupied. */
    IN_USE,
    /** Temporarily unavailable while undergoing maintenance. */
    MAINTENANCE
}
