// WHAT: JUnit 5 unit-test suite that verifies the correctness of DijkstraRouter on a small controlled graph.

// WHY: Coursework requires automated tests to prove the algorithm works; unit tests catch regressions
//      immediately if the routing logic is accidentally broken during refactoring or extension.
//      They also serve as living documentation — each test name describes a specific behaviour
//      that DijkstraRouter must satisfy.

// HOW: A three-node bidirectional test graph is built before each test by @BeforeEach:
//      Node 1 --2km-- Node 2 --3km-- Node 3   (shortest: 1->2->3 = 5 km)
//      Node 1 --------10km---------- Node 3   (direct but longer)
//      Three @Test methods each verify a distinct scenario: the happy-path shortest route,
//      the edge case where source equals destination, and the disconnected-graph case.
//      JUnit 5 assertions (assertTrue, assertEquals, assertFalse) check the RouteResult fields.


package com.mediroute.mediroutebackend.routing.graph; // Declares the package so JUnit can find and run this test class

import com.mediroute.mediroutebackend.routing.model.Node; // Imports the in-memory Node model used to populate the test graph
import com.mediroute.mediroutebackend.routing.model.RouteResult; // Imports RouteResult so test assertions can inspect path, distance, and pathFound flag
import org.junit.jupiter.api.BeforeEach; // Imports @BeforeEach to mark the setup method that runs before every individual test
import org.junit.jupiter.api.Test; // Imports @Test to mark methods as JUnit 5 test cases

import static org.junit.jupiter.api.Assertions.*; // Statically imports all assertion methods (assertTrue, assertEquals, assertFalse, etc.)

class DijkstraRouterTest { // Defines the test class; no @SpringBootTest — tests run as plain unit tests with no Spring context

    private Graph graph; // The shared in-memory graph rebuilt fresh before each test to avoid state leakage between tests
    private DijkstraRouter dijkstra; // The system-under-test — a new instance created before each test

    @BeforeEach // Annotation that tells JUnit to run setUp() before every @Test method in this class
    void setUp() { // Builds the shared three-node test graph and fresh DijkstraRouter instance used in every test
        graph = new Graph(); // Creates a new empty Graph for this test run
        graph.addNode(new Node(1L, "A", "JUNCTION", 0, 0)); // Adds Node 1 (labelled "A") at dummy GPS coordinates — coordinates irrelevant for Dijkstra
        graph.addNode(new Node(2L, "B", "JUNCTION", 0, 0)); // Adds Node 2 (labelled "B") as the intermediate junction
        graph.addNode(new Node(3L, "C", "JUNCTION", 0, 0)); // Adds Node 3 (labelled "C") as the destination node

        graph.addEdge(1L, 2L, 2.0, 3.0, true); // Adds a 2 km bidirectional edge between Node 1 and Node 2
        graph.addEdge(2L, 3L, 3.0, 4.0, true); // Adds a 3 km bidirectional edge between Node 2 and Node 3 (via path total = 5 km)
        graph.addEdge(1L, 3L, 10.0, 12.0, true); // Adds a 10 km direct bidirectional edge between Node 1 and Node 3 (longer alternative)

        dijkstra = new DijkstraRouter(); // Creates the DijkstraRouter instance to be tested
    }

    @Test // Marks this method as a JUnit 5 test case
    void findsShortestPathViaIntermediateNode() { // Verifies that Dijkstra picks the 2-hop route (5 km) over the direct 10 km edge
        RouteResult result = dijkstra.findShortestPath(graph, 1L, 3L); // Runs Dijkstra from Node 1 to Node 3 on the test graph

        assertTrue(result.isPathFound()); // Asserts that a valid route was found (graph is connected)
        assertEquals(5.0, result.getTotalDistanceKm(), 0.001); // Asserts the shortest distance is 5 km (2+3), not 10 km (direct), within floating-point tolerance
        assertEquals(3, result.getPath().size()); // Asserts the path passes through exactly 3 nodes: 1, 2, 3
        assertEquals(1L, result.getPath().get(0)); // Asserts the path starts at Node 1 (source)
        assertEquals(2L, result.getPath().get(1)); // Asserts the path passes through Node 2 (the optimal intermediate junction)
        assertEquals(3L, result.getPath().get(2)); // Asserts the path ends at Node 3 (destination)
    }

    @Test // Marks this method as a JUnit 5 test case
    void sourceEqualsDestinationReturnsZeroDistancePath() { // Verifies the edge case where source and destination are the same node
        RouteResult result = dijkstra.findShortestPath(graph, 1L, 1L); // Runs Dijkstra with source = destination = Node 1

        assertTrue(result.isPathFound()); // Asserts that a path is still reported as found (trivially — stay in place)
        assertEquals(0.0, result.getTotalDistanceKm(), 0.001); // Asserts the total distance is zero because no travel is needed
        assertEquals(1, result.getPath().size()); // Asserts the path contains only one node (the source/destination itself)
    }

    @Test // Marks this method as a JUnit 5 test case
    void disconnectedGraphReturnsNoPathFound() { // Verifies that Dijkstra correctly handles an unreachable destination
        Graph disconnected = new Graph(); // Creates a new isolated graph where Node 1 and Node 2 share no edges
        disconnected.addNode(new Node(1L, "A", "JUNCTION", 0, 0)); // Adds Node 1 to the disconnected graph with no edges
        disconnected.addNode(new Node(2L, "B", "JUNCTION", 0, 0)); // Adds Node 2 to the disconnected graph with no edges

        RouteResult result = dijkstra.findShortestPath(disconnected, 1L, 2L); // Runs Dijkstra between two isolated nodes that have no connecting path

        assertFalse(result.isPathFound()); // Asserts that pathFound is false since Node 2 is unreachable from Node 1
        assertTrue(result.getPath().isEmpty()); // Asserts the path list is empty because no route exists
    }
}
