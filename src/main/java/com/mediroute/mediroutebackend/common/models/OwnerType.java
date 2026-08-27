package com.mediroute.mediroutebackend.common.models;

// This enum defines the facility type that owns a medical resource.
// It tells the system whether a resource belongs to a hospital or a depot.
// That information is important because the same owner ID value is interpreted differently depending on the owner type.
public enum OwnerType {
    /** The resource belongs to a hospital. */
    HOSPITAL,
    /** The resource belongs to an ambulance depot. */
    DEPOT
}
