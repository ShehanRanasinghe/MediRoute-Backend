// WHAT  : JPA entity that represents a directed road segment (edge) connecting two NetworkNodes in the graph.

// WHY   : Dijkstra and A* traverse edges to calculate shortest paths; each road segment must be persisted
//         so the graph can be reconstructed from the Supabase database at application startup.

// HOW   : Annotated with @Entity and mapped to the "road_edge" table. Each row stores foreign-key
//         references to two NetworkNode records (fromNode and toNode), the segment's distance in
//         kilometres, estimated travel time in minutes, and a flag indicating whether the road is
//         traversable in both directions. GraphLoaderService reads all rows to build the in-memory graph.

package com.mediroute.mediroutebackend.common.models; // Declares the package this class belongs to

import jakarta.persistence.*; // Imports all JPA annotations required for ORM mapping

@Entity // Marks this class as a JPA-managed database entity
@Table(name = "road_edge") // Maps this entity to the "road_edge" table in the database
public class RoadEdge { // Defines the RoadEdge class representing one directed road segment between two nodes

    @Id // Marks 'id' as the primary key of the table
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID using the database's identity column
    private Long id; // Stores the unique database primary key for this road segment

    @ManyToOne(fetch = FetchType.LAZY) // Declares a many-to-one relationship; loaded lazily to avoid unnecessary joins
    @JoinColumn(name = "from_node_id", nullable = false) // Maps this field to the "from_node_id" FK column, cannot be null
    private NetworkNode fromNode; // The starting node of this road segment (origin of the directed edge)

    @ManyToOne(fetch = FetchType.LAZY) // Declares a many-to-one relationship; loaded lazily to avoid unnecessary joins
    @JoinColumn(name = "to_node_id", nullable = false) // Maps this field to the "to_node_id" FK column, cannot be null
    private NetworkNode toNode; // The ending node of this road segment (destination of the directed edge)

    @Column(name = "distance_km", nullable = false) // Maps to "distance_km" column; used as the edge weight in Dijkstra
    private double distanceKm; // Physical distance of this road segment in kilometres

    @Column(name = "travel_time_minutes", nullable = false) // Maps to "travel_time_minutes" column; alternative weight for time-based routing
    private double travelTimeMinutes; // Estimated travel time along this segment in minutes

    @Column(nullable = false) // Maps 'bidirectional' to a NOT NULL boolean column
    private boolean bidirectional = true; // If true, GraphLoaderService also adds the reverse edge (toNode → fromNode)

    public RoadEdge() { // No-arg constructor required by JPA to instantiate entities via reflection
    }

    public Long getId() { return id; } // Returns the database primary key of this road segment
    public void setId(Long id) { this.id = id; } // Sets the primary key (normally managed by JPA, used in tests)
    public NetworkNode getFromNode() { return fromNode; } // Returns the origin NetworkNode of this edge
    public void setFromNode(NetworkNode fromNode) { this.fromNode = fromNode; } // Sets the origin NetworkNode
    public NetworkNode getToNode() { return toNode; } // Returns the destination NetworkNode of this edge
    public void setToNode(NetworkNode toNode) { this.toNode = toNode; } // Sets the destination NetworkNode
    public double getDistanceKm() { return distanceKm; } // Returns the road length in kilometres (primary edge weight)
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; } // Updates the distance in kilometres
    public double getTravelTimeMinutes() { return travelTimeMinutes; } // Returns the estimated travel time in minutes
    public void setTravelTimeMinutes(double travelTimeMinutes) { this.travelTimeMinutes = travelTimeMinutes; } // Updates the travel time in minutes
    public boolean isBidirectional() { return bidirectional; } // Returns true if this segment is traversable in both directions
    public void setBidirectional(boolean bidirectional) { this.bidirectional = bidirectional; } // Sets whether this road is two-way
}
