package com.mediroute.mediroutebackend.routing.service;

import com.mediroute.mediroutebackend.routing.graph.AStarRouter;
import com.mediroute.mediroutebackend.routing.graph.DijkstraRouter;
import com.mediroute.mediroutebackend.routing.graph.Graph;
import com.mediroute.mediroutebackend.routing.graph.GraphLoaderService;
import com.mediroute.mediroutebackend.routing.model.Node;
import com.mediroute.mediroutebackend.routing.model.RouteRequest;
import com.mediroute.mediroutebackend.routing.model.RouteResult;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service component connecting the shared Graph with Dijkstra and A* routing engines.
 * Loads graph data from Supabase on startup and provides fallback network capabilities.
 */
@Service
public class RoutingService {

    // The graph loader reads the saved road network from the database and converts it into an in-memory graph.
    @Autowired
    private GraphLoaderService graphLoaderService;

    // These router instances are reused so each request can run either Dijkstra or A* without rebuilding the objects.
    private final DijkstraRouter dijkstraRouter = new DijkstraRouter();
    private final AStarRouter aStarRouter = new AStarRouter();
    private Graph graph;

    /**
     * Initializes the in-memory graph from Supabase database tables.
     * Builds a hardcoded 5-node sample graph if the database is unpopulated.
     */
    @PostConstruct
    public void initGraph() {
        graph = graphLoaderService.loadFromDatabase();

        // Fall back to sample graph if database contains no records
        if (graph.nodeCount() == 0) {
            System.out.println("[RoutingService] No data found in Supabase - using fallback sample graph. "
                    + "Run database/schema.sql and database/seed-data.sql to load real data.");
            graph = buildFallbackSampleGraph();
        } else {
            System.out.println("[RoutingService] Loaded " + graph.nodeCount() + " nodes and "
                    + graph.edgeCount() + " edges from Supabase.");
        }
    }

    /**
     * Constructs a sample road graph for testing without active DB seeding.
     *
     * @return Initialized fallback Graph object.
     */
    private Graph buildFallbackSampleGraph() {
        Graph sample = new Graph();

        // Add core nodes (hospitals, junctions, ambulance depots)
        sample.addNode(new Node(1L, "City Hospital", "HOSPITAL", 6.9271, 79.8612));
        sample.addNode(new Node(2L, "Junction A", "JUNCTION", 6.9310, 79.8650));
        sample.addNode(new Node(3L, "Junction B", "JUNCTION", 6.9350, 79.8700));
        sample.addNode(new Node(4L, "Ambulance Depot 1", "DEPOT", 6.9200, 79.8550));
        sample.addNode(new Node(5L, "General Hospital", "HOSPITAL", 6.9400, 79.8750));

        // Add bidirectional road network edges
        sample.addEdge(4L, 2L, 2.5, 4.0, true);
        sample.addEdge(2L, 1L, 1.8, 3.0, true);
        sample.addEdge(2L, 3L, 3.2, 5.0, true);
        sample.addEdge(3L, 5L, 2.0, 3.5, true);
        sample.addEdge(4L, 3L, 5.0, 8.0, true);
        return sample;
    }

    /**
     * Computes the shortest path using the requested algorithm strategy.
     * Defaults to Dijkstra if no specific algorithm is specified.
     *
     * @param request Route parameters including source, destination, and algorithm choice.
     * @return RouteResult detailing execution path and distance.
     */
    public RouteResult computeRoute(RouteRequest request) {
        if (request.getAlgorithm() != null && request.getAlgorithm().equalsIgnoreCase("astar")) {
            return aStarRouter.findShortestPath(graph, request.getSourceId(), request.getDestinationId());
        }
        return dijkstraRouter.findShortestPath(graph, request.getSourceId(), request.getDestinationId());
    }

    /**
     * Executes both Dijkstra and A* search concurrently to allow side-by-side performance evaluation.
     *
     * @param request Target source and destination query.
     * @return Map containing both "dijkstra" and "astar" RouteResult outputs.
     */
    public Map<String, RouteResult> compareAlgorithms(RouteRequest request) {
        Map<String, RouteResult> results = new HashMap<>();
        results.put("dijkstra", dijkstraRouter.findShortestPath(graph, request.getSourceId(), request.getDestinationId()));
        results.put("astar", aStarRouter.findShortestPath(graph, request.getSourceId(), request.getDestinationId()));
        return results;
    }

    public Graph getGraph() {
        return graph;
    }
}