// WHAT: A plain Java object that represents one weighted directed connection between two graph nodes.

// WHY: Dijkstra and A* need to read the cost of each road segment (distance and travel time) while
//      traversing the adjacency list; this lightweight class holds exactly those two weights plus the
//      destination node ID, with no JPA or database dependency at all.

// HOW: When Graph.addEdge() is called, it creates an Edge instance and appends it to the adjacency
//      list of the origin node. At traversal time, the router calls Graph.getNeighbors() to get the
//      list of Edge objects, reads distanceKm as the edge weight for Dijkstra, and hands both
//      distanceKm and travelTimeMinutes to RouteResult for the response payload.
//      Kept intentionally separate from RoadEdge (the JPA entity) so algorithm code never touches Hibernate.


package com.mediroute.mediroutebackend.routing.model; // Declares the package this class belongs to

public class Edge { // Defines the Edge class that holds the cost and destination of one road segment in the in-memory graph

    private Long targetNodeId; // ID of the destination node that this edge leads to
    private double distanceKm; // Length of this road segment in kilometres; used as the primary edge weight by Dijkstra
    private double travelTimeMinutes; // Estimated travel time in minutes; returned in the route response alongside distance

    public Edge() { // No-arg constructor required for frameworks and serialisation that instantiate via reflection
    }

    public Edge(Long targetNodeId, double distanceKm, double travelTimeMinutes) { // Convenience constructor called by Graph.addEdge() to create a fully populated edge in one step
        this.targetNodeId = targetNodeId; // Sets the ID of the node this edge points to
        this.distanceKm = distanceKm; // Sets the kilometre distance weight of this road segment
        this.travelTimeMinutes = travelTimeMinutes; // Sets the travel time weight of this road segment
    }

    public Long getTargetNodeId() { return targetNodeId; } // Returns the destination node ID; read by Dijkstra/A* to advance to the next node
    public void setTargetNodeId(Long targetNodeId) { this.targetNodeId = targetNodeId; } // Sets the destination node ID
    public double getDistanceKm() { return distanceKm; } // Returns the road length in km; used as the relaxation cost in shortest-path algorithms
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; } // Updates the kilometre distance of this edge
    public double getTravelTimeMinutes() { return travelTimeMinutes; } // Returns the travel time in minutes; included in the RouteResult response
    public void setTravelTimeMinutes(double travelTimeMinutes) { this.travelTimeMinutes = travelTimeMinutes; } // Updates the travel time of this edge
}
