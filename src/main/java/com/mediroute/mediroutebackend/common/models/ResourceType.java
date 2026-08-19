package com.mediroute.mediroutebackend.common.models;

/**
 * What: Lists the medical resource types managed by the system.
 * Why: Different incidents and hospitals require different resources.
 * How: Each resource uses one value to identify its purpose.
 */
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
