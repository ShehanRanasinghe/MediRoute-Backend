// WHAT: In-memory weighted directed graph that represents the entire hospital/road network for the routing module.

// WHY: Dijkstra and A* need a data structure they can traverse in microseconds without hitting the database
//      on every step; this class holds all nodes and edges in RAM so algorithms run at full speed.

// HOW: Uses an adjacency list (HashMap<Long, List<Edge>>) instead of an adjacency matrix because the
//      city road network is sparse - each node connects to only a few neighbours, so an adjacency list
//      costs O(V + E) memory versus O(V2) for a matrix. GraphLoaderService populates this graph at
//      startup by reading NetworkNode and RoadEdge rows from Supabase, then passes it to the routers.
//      The static generateRandomConnected() factory method creates synthetic graphs for BenchmarkRunner.


package com.mediroute.mediroutebackend.routing.graph; // Declares the package this class belongs to

import com.mediroute.mediroutebackend.routing.model.Edge; // Imports the in-memory Edge model used in the adjacency list
import com.mediroute.mediroutebackend.routing.model.Node; // Imports the in-memory Node model that represents graph vertices

import java.util.*; // Imports Map, HashMap, List, ArrayList, Collection, Collections, and Random

public class Graph { // Defines the Graph class that holds the entire road network in memory

    private final Map<Long, Node> nodes = new HashMap<>(); // Stores all nodes keyed by their ID for O(1) lookup
    private final Map<Long, List<Edge>> adjacencyList = new HashMap<>(); // Maps each node ID to its list of outgoing edges

    public void addNode(Node node) { // Registers a single node in the graph
        nodes.put(node.getId(), node); // Inserts the node into the node map under its ID
        adjacencyList.putIfAbsent(node.getId(), new ArrayList<>()); // Ensures a (possibly empty) edge list exists for this node
    }

    public void addEdge(Long fromId, Long toId, double distanceKm, double travelTimeMinutes, boolean bidirectional) { // Adds a weighted directed edge from one node to another
        adjacencyList.computeIfAbsent(fromId, k -> new ArrayList<>()) // Gets or creates the edge list for the origin node
                .add(new Edge(toId, distanceKm, travelTimeMinutes)); // Appends the forward edge (fromId -> toId) with its weights

        if (bidirectional) { // Checks whether this road is traversable in both directions
            adjacencyList.computeIfAbsent(toId, k -> new ArrayList<>()) // Gets or creates the edge list for the destination node
                    .add(new Edge(fromId, distanceKm, travelTimeMinutes)); // Appends the reverse edge (toId -> fromId) with the same weights
        }
    }

    public List<Edge> getNeighbors(Long nodeId) { // Returns all edges reachable from the given node; used by Dijkstra and A* during traversal
        return adjacencyList.getOrDefault(nodeId, Collections.emptyList()); // Returns the node's edge list, or an empty list if no edges exist
    }

    public Node getNode(Long nodeId) { // Retrieves a single node by its ID; used by routers to access GPS coordinates
        return nodes.get(nodeId); // Looks up the node in the node map and returns it (null if not found)
    }

    public Collection<Node> getAllNodes() { // Returns every node in the graph; used by GraphLoaderService and the ping endpoint
        return nodes.values(); // Returns the entire collection of Node objects stored in the map
    }

    public boolean containsNode(Long nodeId) { // Checks whether a node with the given ID exists in the graph
        return nodes.containsKey(nodeId); // Returns true if the node map contains an entry for this ID
    }

    public int nodeCount() { // Returns the total number of nodes in the graph; used for logging and benchmark reporting
        return nodes.size(); // Returns the size of the node map
    }

    public int edgeCount() { // Returns the total number of directed edges across all adjacency lists
        return adjacencyList.values().stream().mapToInt(List::size).sum(); // Sums the sizes of all per-node edge lists
    }

    // Generates a random connected graph used by BenchmarkRunner to evaluate algorithm performance at increasing sizes
    public static Graph generateRandomConnected(int numNodes, int extraEdges, long seed) { // Factory method that builds a synthetic graph with a guaranteed spanning chain plus extra random edges
        Graph graph = new Graph(); // Creates a new empty Graph to populate
        Random random = new Random(seed); // Initialises a seeded random generator for reproducible benchmark results

        for (long i = 1; i <= numNodes; i++) { // Iterates over the required number of nodes to create
            double lat = 6.8 + random.nextDouble() * 0.3; // Generates a random latitude within the Colombo region (6.8–7.1°N)
            double lon = 79.8 + random.nextDouble() * 0.3; // Generates a random longitude within the Colombo region (79.8–80.1°E)
            graph.addNode(new Node(i, "Node" + i, "JUNCTION", lat, lon)); // Adds the new node to the graph as a plain junction
        }

        for (long i = 1; i < numNodes; i++) { // Creates a chain of edges (1->2->3...->N) guaranteeing the graph is connected
            double dist = 1 + random.nextDouble() * 5; // Picks a random road length between 1 and 6 km
            graph.addEdge(i, i + 1, dist, dist * 1.5, true); // Adds a bidirectional edge; travel time estimated as 1.5× distance
        }

        for (int i = 0; i < extraEdges; i++) { // Adds the requested number of random extra edges to increase graph density
            long a = 1 + random.nextInt(numNodes); // Picks a random source node
            long b = 1 + random.nextInt(numNodes); // Picks a random destination node
            if (a != b) { // Skips self-loops since an edge from a node to itself has no routing meaning
                double dist = 1 + random.nextDouble() * 5; // Picks a random road length between 1 and 6 km
                graph.addEdge(a, b, dist, dist * 1.5, true); // Adds the extra bidirectional edge to the graph
            }
        }
        return graph; // Returns the fully constructed random graph for use by BenchmarkRunner
    }
}
