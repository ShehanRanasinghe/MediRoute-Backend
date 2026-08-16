package com.mediroute.mediroutebackend.routing.graph;

import com.mediroute.mediroutebackend.routing.model.Node;
import com.mediroute.mediroutebackend.routing.model.RouteResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the A* Search algorithm implementation (AStarRouter).
 * Uses the same test graph layout as DijkstraRouterTest to verify that
 * both algorithms yield identical optimal paths and distance results.
 */
class AStarRouterTest {

    private Graph graph;
    private AStarRouter aStar;

    /**
     * Sets up a small 3-node connected graph before each test execution.
     * Nodes represent locations with coordinates for Haversine heuristic
     * validation.
     */
    @BeforeEach
    void setUp() {
        graph = new Graph();
        graph.addNode(new Node(1L, "A", "JUNCTION", 6.9271, 79.8612));
        graph.addNode(new Node(2L, "B", "JUNCTION", 6.9300, 79.8650));
        graph.addNode(new Node(3L, "C", "JUNCTION", 6.9330, 79.8690));

        // Connect nodes: Path 1->2->3 costs 5.0 km vs direct path 1->3 costing 10.0 km
        graph.addEdge(1L, 2L, 2.0, 3.0, true);
        graph.addEdge(2L, 3L, 3.0, 4.0, true);
        graph.addEdge(1L, 3L, 10.0, 12.0, true);

        aStar = new AStarRouter();
    }

    /**
     * Verifies that A* correctly selects the shortest path (1 -> 2 -> 3 = 5.0km)
     * instead of taking a sub-optimal direct edge (1 -> 3 = 10.0km).
     */
    @Test
    void findsSameOptimalDistanceAsDijkstra() {
        RouteResult result = aStar.findShortestPath(graph, 1L, 3L);

        assertTrue(result.isPathFound());
        assertEquals(5.0, result.getTotalDistanceKm(), 0.001);
        assertEquals(3, result.getPath().size());
    }

    /**
     * Tests the edge case where the source and destination nodes are identical.
     */
    @Test
    void sourceEqualsDestinationReturnsZeroDistancePath() {
        RouteResult result = aStar.findShortestPath(graph, 1L, 1L);

        assertTrue(result.isPathFound());
        assertEquals(0.0, result.getTotalDistanceKm(), 0.001);
    }

    /**
     * Tests behavior on a disconnected graph where no path exists between nodes.
     */
    @Test
    void disconnectedGraphReturnsNoPathFound() {
        Graph disconnected = new Graph();
        disconnected.addNode(new Node(1L, "A", "JUNCTION", 6.9271, 79.8612));
        disconnected.addNode(new Node(2L, "B", "JUNCTION", 6.9400, 79.8800));

        RouteResult result = aStar.findShortestPath(disconnected, 1L, 2L);

        assertFalse(result.isPathFound());
        assertTrue(result.getPath().isEmpty());
    }
}