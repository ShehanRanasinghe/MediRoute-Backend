package com.mediroute.routing.graph;

import com.mediroute.routing.model.Edge;
import com.mediroute.routing.model.Node;
import com.mediroute.routing.model.RouteResult;

import java.util.*;

public class AStarRouter {

    public RouteResult findShortestPath(Graph graph, Long sourceId, Long destinationId) {
        long startTime = System.nanoTime();

        Node destinationNode = graph.getNode(destinationId);

        Map<Long, Double> gScore = new HashMap<>();
        Map<Long, Long> previous = new HashMap<>();
        Set<Long> visited = new HashSet<>();

        // Initialize distances to infinity
        for (Node node : graph.getAllNodes()) {
            gScore.put(node.getId(), Double.MAX_VALUE);
        }
        gScore.put(sourceId, 0.0);

        // Priority queue sorted by fScore (f = g + h)
        PriorityQueue<NodeScore> openSet = new PriorityQueue<>(Comparator.comparingDouble(ns -> ns.fScore));

        openSet.add(new NodeScore(sourceId, heuristic(graph.getNode(sourceId), destinationNode)));

        while (!openSet.isEmpty()) {
            NodeScore current = openSet.poll();

            if (visited.contains(current.nodeId)) {
                continue;
            }
            visited.add(current.nodeId);

            // Reached destination
            if (current.nodeId.equals(destinationId)) {
                break;
            }

            for (Edge edge : graph.getNeighbors(current.nodeId)) {
                if (visited.contains(edge.getTargetNodeId())) {
                    continue;
                }

                // Calculate tentative gScore
                double tentativeG = gScore.get(current.nodeId) + edge.getDistanceKm();

                if (tentativeG < gScore.get(edge.getTargetNodeId())) {
                    gScore.put(edge.getTargetNodeId(), tentativeG);
                    previous.put(edge.getTargetNodeId(), current.nodeId);

                    // Add node with fScore = gScore + heuristic
                    double h = heuristic(graph.getNode(edge.getTargetNodeId()), destinationNode);
                    openSet.add(new NodeScore(edge.getTargetNodeId(), tentativeG + h));
                }
            }
        }

        long endTime = System.nanoTime();

        RouteResult result = new RouteResult();
        result.setAlgorithmUsed("A*");
        result.setExecutionTimeNanos(endTime - startTime);

        Double finalDistance = gScore.get(destinationId);

        // Handle no path found
        if (finalDistance == null || finalDistance == Double.MAX_VALUE) {
            result.setPathFound(false);
            result.setPath(Collections.emptyList());
            return result;
        }

        List<Long> path = reconstructPath(previous, sourceId, destinationId);
        result.setPathFound(true);
        result.setPath(path);
        result.setTotalDistanceKm(finalDistance);
        return result;
    }

    // Haversine formula to calculate straight-line distance
    private double heuristic(Node a, Node b) {
        final double EARTH_RADIUS_KM = 6371.0;

        double lat1 = Math.toRadians(a.getLatitude());
        double lat2 = Math.toRadians(b.getLatitude());
        double deltaLat = Math.toRadians(b.getLatitude() - a.getLatitude());
        double deltaLon = Math.toRadians(b.getLongitude() - a.getLongitude());

        double h = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                        * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(h), Math.sqrt(1 - h));

        return EARTH_RADIUS_KM * c;
    }

    // Reconstruct route backward from destination to source
    private List<Long> reconstructPath(Map<Long, Long> previous, Long sourceId, Long destinationId) {
        LinkedList<Long> path = new LinkedList<>();
        Long current = destinationId;
        path.addFirst(current);

        while (!current.equals(sourceId)) {
            current = previous.get(current);
            if (current == null) {
                return Collections.emptyList();
            }
            path.addFirst(current);
        }
        return path;
    }

    private static class NodeScore {
        final Long nodeId;
        final double fScore;

        NodeScore(Long nodeId, double fScore) {
            this.nodeId = nodeId;
            this.fScore = fScore;
        }
    }
}