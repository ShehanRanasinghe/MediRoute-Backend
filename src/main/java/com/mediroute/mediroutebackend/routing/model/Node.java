// WHAT: A plain Java object that represents a single network node (hospital, depot, or junction)
//       inside the in-memory graph used by the routing algorithms.

// WHY: Dijkstra needs node IDs to traverse edges; A* additionally needs GPS coordinates (latitude and
//      longitude) to compute the Haversine heuristic that estimates remaining distance to the goal.
//      This class carries all of that data in a form the algorithms can access instantly without a
//      database round-trip on every relaxation step.

// HOW: GraphLoaderService reads every NetworkNode row from Supabase, converts each one into a Node
//      instance, and calls Graph.addNode() to register it. Dijkstra and A* then call Graph.getNode()
//      to retrieve Node objects during path reconstruction or heuristic computation.
//      Deliberately kept separate from the JPA entity NetworkNode so algorithm classes have zero
//      dependency on Hibernate — they operate purely on plain data.


package com.mediroute.mediroutebackend.routing.model; // Declares the package this class belongs to

public class Node { // Defines the Node class that holds all routing-relevant data for one graph vertex in memory

    private Long id; // Unique identifier matching the database primary key; used as the graph vertex key
    private String name; // Human-readable label (e.g. "City Hospital") included in the route response
    private String type; // Category string ("HOSPITAL", "DEPOT", or "JUNCTION") used for display and filtering
    private double latitude; // GPS latitude coordinate; read by A* to compute the Haversine straight-line heuristic
    private double longitude; // GPS longitude coordinate; read by A* alongside latitude for heuristic distance estimation

    public Node() { // No-arg constructor required for frameworks and serialisation that instantiate via reflection
    }

    public Node(Long id, String name, String type, double latitude, double longitude) { // Convenience constructor used by GraphLoaderService and generateRandomConnected() to build nodes in one line
        this.id = id; // Assigns the node's unique database ID
        this.name = name; // Assigns the human-readable name for this location
        this.type = type; // Assigns the category string (HOSPITAL / DEPOT / JUNCTION)
        this.latitude = latitude; // Assigns the GPS latitude used by the A* Haversine heuristic
        this.longitude = longitude; // Assigns the GPS longitude used by the A* Haversine heuristic
    }

    public Long getId() { return id; } // Returns the node's unique ID; used as the key in Graph's node map and adjacency list
    public void setId(Long id) { this.id = id; } // Sets the node's unique ID
    public String getName() { return name; } // Returns the human-readable node name; included in the RouteResult path list
    public void setName(String name) { this.name = name; } // Updates the node's display name
    public String getType() { return type; } // Returns the category string; used by the frontend to style node markers differently
    public void setType(String type) { this.type = type; } // Updates the node's category string
    public double getLatitude() { return latitude; } // Returns the GPS latitude; consumed by AStarRouter's haversine() to estimate remaining distance
    public void setLatitude(double latitude) { this.latitude = latitude; } // Updates the latitude coordinate
    public double getLongitude() { return longitude; } // Returns the GPS longitude; consumed by AStarRouter's haversine() alongside latitude
    public void setLongitude(double longitude) { this.longitude = longitude; } // Updates the longitude coordinate

    @Override // Signals that this method overrides the Object.toString() method for a more readable debug output
    public String toString() { // Returns a concise string representation of this node for logging and debugging
        return "Node{id=" + id + ", name='" + name + "'}"; // Formats the node as "Node{id=X, name='Y'}" for easy reading in logs
    }
}
