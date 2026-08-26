package com.mediroute.mediroutebackend.network.algorithm;

import com.mediroute.mediroutebackend.network.model.MSTResult;
import com.mediroute.mediroutebackend.routing.graph.Graph;
import com.mediroute.mediroutebackend.routing.model.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test graph (4 nodes forming a square, plus one diagonal):
 *   1 --1.0-- 2
 *   2 --2.0-- 3
 *   3 --1.0-- 4
 *   4 --2.0-- 1
 *   1 --3.0-- 3   (diagonal, more expensive than the square edges)
 *
 * Cheapest way to connect all 4 nodes: edges 1-2 (1.0), 3-4 (1.0), and
 * either 2-3 or 4-1 (2.0) to join the two pairs together.
 * Expected minimum total weight: 1.0 + 1.0 + 2.0 = 4.0, using 3 edges.
 */
class MSTBuilderTest {

    private final MSTBuilder mstBuilder = new MSTBuilder();

    @Test
    void findsMinimumSpanningTreeWeight() {
        Graph graph = new Graph();
        graph.addNode(new Node(1L, "A", "JUNCTION", 0, 0));
        graph.addNode(new Node(2L, "B", "JUNCTION", 0, 0));
        graph.addNode(new Node(3L, "C", "JUNCTION", 0, 0));
        graph.addNode(new Node(4L, "D", "JUNCTION", 0, 0));

        graph.addEdge(1L, 2L, 1.0, 1.0, true);
        graph.addEdge(2L, 3L, 2.0, 2.0, true);
        graph.addEdge(3L, 4L, 1.0, 1.0, true);
        graph.addEdge(4L, 1L, 2.0, 2.0, true);
        graph.addEdge(1L, 3L, 3.0, 3.0, true); // more expensive diagonal - should NOT be in the MST

        MSTResult result = mstBuilder.buildMST(graph);

        assertTrue(result.isConnected());
        assertEquals(3, result.getEdges().size()); // MST of 4 nodes always has exactly (nodes - 1) edges
        assertEquals(4.0, result.getTotalWeightKm(), 0.001);
    }

    @Test
    void singleNodeGraphProducesEmptyMST() {
        Graph graph = new Graph();
        graph.addNode(new Node(1L, "A", "JUNCTION", 0, 0));

        MSTResult result = mstBuilder.buildMST(graph);

        assertTrue(result.isConnected());
        assertTrue(result.getEdges().isEmpty());
        assertEquals(0.0, result.getTotalWeightKm(), 0.001);
    }

    @Test
    void disconnectedGraphIsReportedAsNotConnected() {
        Graph graph = new Graph();
        graph.addNode(new Node(1L, "A", "JUNCTION", 0, 0));
        graph.addNode(new Node(2L, "B", "JUNCTION", 0, 0));
        // no edge between them - two separate components

        MSTResult result = mstBuilder.buildMST(graph);

        assertFalse(result.isConnected());
    }
}
