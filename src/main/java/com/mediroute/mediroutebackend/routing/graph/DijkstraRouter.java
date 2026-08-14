// WHAT: Implements Dijkstra's shortest-path algorithm over the in-memory Graph to find the minimum-distance
//       route between any two network nodes (hospitals, depots, junctions) in the MediRoute system.

// WHY: Ambulance dispatch requires the shortest road route in real time; Dijkstra guarantees the globally
//      optimal result for graphs with non-negative edge weights (road distances are always >= 0).
//      It is one of the two algorithms compared by the routing module (the other being A*), letting
//      RoutingService and the frontend /compare endpoint show both results side-by-side.

// HOW: Uses a min-heap (PriorityQueue) ordered by tentative distance so the node with the smallest
//      known distance is always processed next, giving O((V + E) log V) time and O(V + E) space.
//      At each step the algorithm relaxes outgoing edges, updating the distance map and predecessor
//      map whenever a shorter path is found. Once the destination is settled, reconstructPath()
//      walks the predecessor map backwards from destination to source to build the ordered path list.


package com.mediroute.mediroutebackend.routing.graph; // Declares the package this class belongs to

import com.mediroute.mediroutebackend.routing.model.Edge; // Imports the in-memory Edge model to read edge weights during relaxation
import com.mediroute.mediroutebackend.routing.model.Node; // Imports the in-memory Node model to iterate over all graph nodes
import com.mediroute.mediroutebackend.routing.model.RouteResult; // Imports the RouteResult DTO that is populated and returned to RoutingService after the algorithm completes

import java.util.*; // Imports Map, HashMap, Set, HashSet, List, LinkedList, PriorityQueue, Comparator, Collections

public class DijkstraRouter { // Defines the DijkstraRouter class that exposes a single findShortestPath() method

    public RouteResult findShortestPath(Graph graph, Long sourceId, Long destinationId) { // Entry point called by RoutingService; returns a RouteResult with the shortest path and metadata
        long startTime = System.nanoTime(); // Records the wall-clock start time in nanoseconds for execution-time measurement

        Map<Long, Double> distances = new HashMap<>(); // Maps each node ID to its current best-known tentative distance from the source
        Map<Long, Long> previous = new HashMap<>(); // Maps each node ID to the preceding node ID on the shortest path found so far
        Set<Long> visited = new HashSet<>(); // Tracks nodes whose shortest distance has been finalised and will not be updated again

        for (Node node : graph.getAllNodes()) { // Iterates over every node in the graph to initialise distances
            distances.put(node.getId(), Double.MAX_VALUE); // Sets each node's initial distance to infinity (no path known yet)
        }
        distances.put(sourceId, 0.0); // The source node's distance to itself is zero — the algorithm starts here

        PriorityQueue<NodeDistance> minHeap =
                new PriorityQueue<>(Comparator.comparingDouble(nd -> nd.distance)); // Creates a min-heap that always yields the unvisited node with the smallest tentative distance
        minHeap.add(new NodeDistance(sourceId, 0.0)); // Seeds the heap with the source node at distance 0

        while (!minHeap.isEmpty()) { // Continues until no reachable unvisited nodes remain or the destination is settled
            NodeDistance current = minHeap.poll(); // Extracts the node with the smallest tentative distance from the heap (O(log V))

            if (visited.contains(current.nodeId)) { // Checks if this node was already settled by a previous shorter path
                continue; // Skips stale heap entries that were superseded by a shorter path discovered later
            }
            visited.add(current.nodeId); // Marks this node as settled — its shortest distance is now final

            if (current.nodeId.equals(destinationId)) { // Checks if the destination has just been settled
                break; // Stops early because the optimal path to the destination is already known
            }

            for (Edge edge : graph.getNeighbors(current.nodeId)) { // Iterates over every outgoing edge from the current node (edge relaxation step)
                if (visited.contains(edge.getTargetNodeId())) { // Checks if the neighbour has already been settled
                    continue; // Skips settled neighbours because their shortest distance cannot be improved
                }
                double candidateDistance = distances.get(current.nodeId) + edge.getDistanceKm(); // Calculates the tentative distance to the neighbour via the current node
                if (candidateDistance < distances.get(edge.getTargetNodeId())) { // Checks if this path is shorter than the previously known best path to the neighbour
                    distances.put(edge.getTargetNodeId(), candidateDistance); // Updates the neighbour's best-known distance with the shorter value
                    previous.put(edge.getTargetNodeId(), current.nodeId); // Records the current node as the predecessor on the optimal path to this neighbour
                    minHeap.add(new NodeDistance(edge.getTargetNodeId(), candidateDistance)); // Pushes the neighbour with its updated distance into the heap for future processing
                }
            }
        }

        long endTime = System.nanoTime(); // Records the wall-clock end time after the main loop finishes

        RouteResult result = new RouteResult(); // Creates the response object that will be returned to RoutingService
        result.setAlgorithmUsed("Dijkstra"); // Labels the result so the frontend can display which algorithm was used
        result.setExecutionTimeNanos(endTime - startTime); // Stores the elapsed time in nanoseconds for the experimental evaluation comparison

        Double finalDistance = distances.get(destinationId); // Reads the shortest distance found for the destination node
        if (finalDistance == null || finalDistance == Double.MAX_VALUE) { // Checks whether the destination was unreachable (still at initial infinity value)
            result.setPathFound(false); // Signals that no valid route exists between source and destination
            result.setPath(Collections.emptyList()); // Returns an empty path list since no route was found
            return result; // Returns the result early — no path to reconstruct
        }

        List<Long> path = reconstructPath(previous, sourceId, destinationId); // Walks the predecessor map to build the ordered list of node IDs from source to destination
        result.setPathFound(true); // Signals that a valid route was found
        result.setPath(path); // Attaches the ordered node-ID path to the result
        result.setTotalDistanceKm(finalDistance); // Attaches the total route distance in kilometres to the result
        return result; // Returns the fully populated RouteResult to the caller
    }

    private List<Long> reconstructPath(Map<Long, Long> previous, Long sourceId, Long destinationId) { // Traces the predecessor chain from destination back to source to produce the forward-ordered path
        LinkedList<Long> path = new LinkedList<>(); // Uses a LinkedList so prepending (addFirst) is O(1) — avoids O(n) shifts of ArrayList
        Long current = destinationId; // Starts at the destination and walks backwards through the predecessor map
        path.addFirst(current); // Adds the destination as the last node (will become the tail of the forward path)

        while (!current.equals(sourceId)) { // Continues walking backwards until the source node is reached
            current = previous.get(current); // Moves to the predecessor of the current node on the shortest path
            if (current == null) { // Checks for a gap in the predecessor map (graph is disconnected or path was never found)
                return Collections.emptyList(); // Returns an empty list as a safe fallback for the disconnected case
            }
            path.addFirst(current); // Prepends the predecessor so the final list is in source-to-destination order
        }
        return path; // Returns the complete ordered list of node IDs representing the shortest route
    }

    private static class NodeDistance { // Private inner class that pairs a node ID with a tentative distance for use in the min-heap
        final Long nodeId; // The ID of the graph node this entry represents
        final double distance; // The tentative shortest distance from the source to this node at the time of insertion

        NodeDistance(Long nodeId, double distance) { // Constructor that sets both fields for a new heap entry
            this.nodeId = nodeId; // Assigns the node ID to this heap entry
            this.distance = distance; // Assigns the tentative distance to this heap entry
        }
    }
}
