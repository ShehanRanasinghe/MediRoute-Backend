// WHAT: JPA entity that represents a single geographic point (node) in the hospital/road network graph.

// WHY: Dijkstra and A* both operate on a graph; every hospital, depot, and road junction must exist
//      as a persistent database record so the graph can be loaded at runtime from Supabase.

// HOW: Annotated with @Entity so Spring Data JPA maps this class to the "network_node" table in PostgreSQL.
//      Each instance holds a name, a category (NodeType), and GPS coordinates (latitude/longitude).
//      Used by Task 1 (Route Optimization) and Task 3 (Network Analysis).

package com.mediroute.mediroutebackend.common.models; // Declares the package this class belongs to

import jakarta.persistence.*; // Imports all JPA annotations needed for ORM mapping

@Entity // Marks this class as a JPA-managed database entity
@Table(name = "network_node") // Maps this entity to the "network_node" table in the database
public class NetworkNode { // Defines the NetworkNode class representing one graph node

    @Id // Marks 'id' as the primary key of the table
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID using the database's identity column
    private Long id; // Stores the unique database primary key for this node

    @Column(nullable = false, length = 150) // Maps 'name' to a NOT NULL varchar(150) column
    private String name; // Human-readable label for the node (e.g. "City Hospital", "Central Depot")

    @Enumerated(EnumType.STRING) // Stores the enum constant as its string name instead of an ordinal number
    @Column(name = "node_type", nullable = false, length = 20) // Maps to the "node_type" column, cannot be null
    private NodeType nodeType; // Classifies this node as HOSPITAL, DEPOT, or JUNCTION (see NodeType enum)

    @Column(nullable = false) // Maps 'latitude' to a NOT NULL numeric column
    private double latitude; // GPS latitude of this node; used by A* Haversine heuristic for distance estimation

    @Column(nullable = false) // Maps 'longitude' to a NOT NULL numeric column
    private double longitude; // GPS longitude of this node; combined with latitude to locate the node on a map

    public NetworkNode() { // No-arg constructor required by JPA to instantiate entities via reflection
    }

    public NetworkNode(String name, NodeType nodeType, double latitude, double longitude) { // Convenience constructor for creating nodes in code or tests
        this.name = name; // Assigns the provided name to this node
        this.nodeType = nodeType; // Assigns the category (HOSPITAL / DEPOT / JUNCTION)
        this.latitude = latitude; // Assigns the GPS latitude coordinate
        this.longitude = longitude; // Assigns the GPS longitude coordinate
    }

    public Long getId() { return id; } // Returns the database primary key of this node
    public void setId(Long id) { this.id = id; } // Sets the primary key (normally managed by JPA, used in tests)
    public String getName() { return name; } // Returns the human-readable name of this node
    public void setName(String name) { this.name = name; } // Updates the name of this node
    public NodeType getNodeType() { return nodeType; } // Returns the category enum value for this node
    public void setNodeType(NodeType nodeType) { this.nodeType = nodeType; } // Sets the category of this node
    public double getLatitude() { return latitude; } // Returns the GPS latitude used for Haversine calculations
    public void setLatitude(double latitude) { this.latitude = latitude; } // Updates the latitude coordinate
    public double getLongitude() { return longitude; } // Returns the GPS longitude used for Haversine calculations
    public void setLongitude(double longitude) { this.longitude = longitude; } // Updates the longitude coordinate
}
