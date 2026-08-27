// This service reads the saved road network from the database and turns it into the in-memory graph used by the route search.
// It acts as the connection between the database entities and the algorithm classes that only understand plain Java objects.
// Without this step, the routing code would have to work with database records directly and would run much slower.

package com.mediroute.mediroutebackend.routing.graph; // Declares the package this class belongs to

import com.mediroute.mediroutebackend.common.models.NetworkNode; // Imports the JPA entity representing a node row in the database
import com.mediroute.mediroutebackend.common.models.RoadEdge; // Imports the JPA entity representing a road-edge row in the database
import com.mediroute.mediroutebackend.common.models.repository.NetworkNodeRepository; // Imports the Spring Data repository used to fetch all NetworkNode rows
import com.mediroute.mediroutebackend.common.models.repository.RoadEdgeRepository; // Imports the Spring Data repository used to fetch all RoadEdge rows
import com.mediroute.mediroutebackend.routing.model.Node; // Imports the in-memory Node model that the Graph and algorithm classes understand
import org.springframework.beans.factory.annotation.Autowired; // Imports @Autowired so Spring can inject the repository beans automatically
import org.springframework.stereotype.Component; // Imports @Component to register this class as a Spring-managed bean

import java.util.List; // Imports List to hold the collections of DB entities returned by findAll()

@Component // Registers GraphLoaderService as a Spring bean so it can be injected into RoutingService
public class GraphLoaderService { // Defines the service class responsible for building the in-memory Graph from the database

    @Autowired // Tells Spring to inject the NetworkNodeRepository bean into this field automatically
    private NetworkNodeRepository networkNodeRepository; // Repository used to load all network node rows from the Supabase database

    @Autowired // Tells Spring to inject the RoadEdgeRepository bean into this field automatically
    private RoadEdgeRepository roadEdgeRepository; // Repository used to load all road edge rows from the Supabase database

    public Graph loadFromDatabase() { // Builds and returns a complete in-memory Graph by reading all nodes and edges from Supabase
        Graph graph = new Graph(); // Creates a new empty Graph instance to be populated with DB data

        List<NetworkNode> nodes = networkNodeRepository.findAll(); // Fetches every NetworkNode row from the database in a single query
        for (NetworkNode dbNode : nodes) { // Iterates over each database node to convert and register it in the graph
            graph.addNode(new Node( // Converts the JPA NetworkNode entity into a lightweight in-memory Node and adds it to the graph
                    dbNode.getId(), // Passes the database primary key as the in-memory node ID so graph keys match DB keys
                    dbNode.getName(), // Passes the human-readable location name (e.g. "City Hospital") to the in-memory node
                    dbNode.getNodeType().name(), // Converts the NodeType enum to its string name ("HOSPITAL", "DEPOT", or "JUNCTION")
                    dbNode.getLatitude(), // Passes the GPS latitude needed by A*'s Haversine heuristic
                    dbNode.getLongitude() // Passes the GPS longitude needed by A*'s Haversine heuristic
            ));
        }

        List<RoadEdge> edges = roadEdgeRepository.findAll(); // Fetches every RoadEdge row from the database in a single query
        for (RoadEdge dbEdge : edges) { // Iterates over each database edge to register it in the graph
            graph.addEdge( // Calls Graph.addEdge() to store the road segment in the in-memory adjacency list
                    dbEdge.getFromNode().getId(), // Passes the origin node ID (from the FK relationship) as the edge source
                    dbEdge.getToNode().getId(), // Passes the destination node ID (from the FK relationship) as the edge target
                    dbEdge.getDistanceKm(), // Passes the road length in km as the primary edge weight for Dijkstra
                    dbEdge.getTravelTimeMinutes(), // Passes the travel time in minutes as the secondary edge weight for the response payload
                    dbEdge.isBidirectional() // If true, Graph.addEdge() also adds the reverse edge so both directions are traversable
            );
        }

        return graph; // Returns the fully populated Graph to RoutingService for use by DijkstraRouter and AStarRouter
    }
}
