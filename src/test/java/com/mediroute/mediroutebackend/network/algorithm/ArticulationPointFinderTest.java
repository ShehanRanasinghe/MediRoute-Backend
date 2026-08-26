package com.mediroute.mediroutebackend.network.algorithm;

import com.mediroute.mediroutebackend.network.model.CriticalNodeResult;
import com.mediroute.mediroutebackend.routing.graph.Graph;
import com.mediroute.mediroutebackend.routing.model.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArticulationPointFinderTest {

    private final ArticulationPointFinder finder = new ArticulationPointFinder();

    @Test
    void pathGraphHasOneArticulationPoint() {
        // A - B - C : removing B disconnects A from C
        Graph graph = new Graph();
        graph.addNode(new Node(1L, "A", "JUNCTION", 0, 0));
        graph.addNode(new Node(2L, "B", "JUNCTION", 0, 0));
        graph.addNode(new Node(3L, "C", "JUNCTION", 0, 0));
        graph.addEdge(1L, 2L, 1.0, 1.0, true);
        graph.addEdge(2L, 3L, 1.0, 1.0, true);

        CriticalNodeResult result = finder.findArticulationPoints(graph);

        assertEquals(1, result.getCriticalNodeIds().size());
        assertTrue(result.getCriticalNodeIds().contains(2L));
    }

    @Test
    void triangleGraphHasNoArticulationPoints() {
        // A - B - C - A (cycle): removing any single node still leaves the other two connected
        Graph graph = new Graph();
        graph.addNode(new Node(1L, "A", "JUNCTION", 0, 0));
        graph.addNode(new Node(2L, "B", "JUNCTION", 0, 0));
        graph.addNode(new Node(3L, "C", "JUNCTION", 0, 0));
        graph.addEdge(1L, 2L, 1.0, 1.0, true);
        graph.addEdge(2L, 3L, 1.0, 1.0, true);
        graph.addEdge(3L, 1L, 1.0, 1.0, true);

        CriticalNodeResult result = finder.findArticulationPoints(graph);

        assertTrue(result.getCriticalNodeIds().isEmpty());
    }

    @Test
    void mediRouteSampleGraphHasTwoArticulationPoints() {
        // Same graph as RoutingService's fallback sample: Junction A (2) and
        // Junction B (3) are each the ONLY route to a hospital.
        Graph graph = new Graph();
        graph.addNode(new Node(1L, "City Hospital", "HOSPITAL", 0, 0));
        graph.addNode(new Node(2L, "Junction A", "JUNCTION", 0, 0));
        graph.addNode(new Node(3L, "Junction B", "JUNCTION", 0, 0));
        graph.addNode(new Node(4L, "Ambulance Depot 1", "DEPOT", 0, 0));
        graph.addNode(new Node(5L, "General Hospital", "HOSPITAL", 0, 0));
        graph.addEdge(4L, 2L, 2.5, 4.0, true);
        graph.addEdge(2L, 1L, 1.8, 3.0, true);
        graph.addEdge(2L, 3L, 3.2, 5.0, true);
        graph.addEdge(3L, 5L, 2.0, 3.5, true);
        graph.addEdge(4L, 3L, 5.0, 8.0, true);

        CriticalNodeResult result = finder.findArticulationPoints(graph);

        assertEquals(2, result.getCriticalNodeIds().size());
        assertTrue(result.getCriticalNodeIds().contains(2L)); // Junction A - only route to City Hospital
        assertTrue(result.getCriticalNodeIds().contains(3L)); // Junction B - only route to General Hospital
    }
}
