package com.mediroute.routing.graph;

import com.mediroute.routing.model.Node;
import com.mediroute.routing.model.RouteResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 
 *
 * Uses the same test graph shape as DijkstraRouterTest so results can be
 * directly compared - both algorithms MUST return the same optimal distance.
 */
class AStarRouterTest {

    private Graph graph;
    private AStarRouter aStar;

    @BeforeEach
    void setUp() {
        graph = new Graph();
        graph.addNode(new Node(1L, "A", "JUNCTION", 6.9271, 79.8612));
        graph.addNode(new Node(2L, "B", "JUNCTION", 6.9300, 79.8650));
        graph.addNode(new Node(3L, "C", "JUNCTION", 6.9330, 79.8690));

        graph.addEdge(1L, 2L, 2.0, 3.0, true);
        graph.addEdge(2L, 3L, 3.0, 4.0, true);
        graph.addEdge(1L, 3L, 10.0, 12.0, true);

        aStar = new AStarRouter();
    }

    @Test
    void findsSameOptimalDistanceAsDijkstra() {
        RouteResult result = aStar.findShortestPath(graph, 1L, 3L);

        assertTrue(result.isPathFound());
        assertEquals(5.0, result.getTotalDistanceKm(), 0.001);
        assertEquals(3, result.getPath().size());
    }

    @Test
    void sourceEqualsDestinationReturnsZeroDistancePath() {
        RouteResult result = aStar.findShortestPath(graph, 1L, 1L);

        assertTrue(result.isPathFound());
        assertEquals(0.0, result.getTotalDistanceKm(), 0.001);
    }

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
