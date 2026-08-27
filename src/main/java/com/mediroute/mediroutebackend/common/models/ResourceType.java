package com.mediroute.mediroutebackend.common.models;

// This enum defines the types of medical resources the system can manage.
// Each type represents a different asset, such as an ambulance or a bed, that may be needed by a patient.
// The allocation logic uses this value to decide which resource pool should be checked for a request.
public enum ResourceType {
    /** Emergency transport vehicle. */
    AMBULANCE,
    /** Bed equipped for intensive care. */
    ICU_BED,
    /** Standard inpatient ward bed. */
    WARD_BED,
    /** Mechanical breathing-support device. */
    VENTILATOR
}
