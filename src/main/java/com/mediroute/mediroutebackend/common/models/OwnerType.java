package com.mediroute.mediroutebackend.common.models;

/**
 * What: Lists the facility types that can own a medical resource.
 * Why: A resource must be connected to the correct kind of owner.
 * How: The selected value defines how the resource owner ID is interpreted.
 */
public enum OwnerType { 
    /** The resource belongs to a hospital. */
    HOSPITAL, 
    /** The resource belongs to an ambulance depot. */
    DEPOT 
}
