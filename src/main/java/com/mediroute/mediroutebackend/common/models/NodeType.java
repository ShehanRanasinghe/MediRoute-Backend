// WHAT  : A simple Java enum that lists the three valid categories a network node can belong to.

// WHY   : Routing decisions and UI display need to distinguish hospitals (destinations),
//         depots (ambulance bases), and junctions (plain road intersections). Storing a typed
//         enum prevents invalid category strings from entering the database.

// HOW   : Declared as a Java enum so the compiler enforces valid values at compile time.
//         The @Enumerated(EnumType.STRING) annotation on NetworkNode tells JPA to persist
//         each constant as its exact name ("HOSPITAL", "DEPOT", "JUNCTION") in the column.

package com.mediroute.mediroutebackend.common.models; // Declares the package this enum belongs to

public enum NodeType { // Defines the set of allowed node categories in the road/hospital network
    HOSPITAL, // Represents a hospital — a valid ambulance destination or pickup point
    DEPOT,    // Represents an ambulance depot — the starting base for routing calculations
    JUNCTION  // Represents a plain road junction — an intermediate point with no special role
}
