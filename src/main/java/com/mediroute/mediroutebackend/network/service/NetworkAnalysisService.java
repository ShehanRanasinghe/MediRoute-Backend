package com.mediroute.mediroutebackend.network.service;

import com.mediroute.mediroutebackend.network.algorithm.ArticulationPointFinder;
import com.mediroute.mediroutebackend.network.algorithm.CentralityCalculator;
import com.mediroute.mediroutebackend.network.algorithm.MSTBuilder;
import com.mediroute.mediroutebackend.network.model.CentralityResult;
import com.mediroute.mediroutebackend.network.model.CriticalNodeResult;
import com.mediroute.mediroutebackend.network.model.MSTResult;
import com.mediroute.mediroutebackend.routing.graph.Graph;
import com.mediroute.mediroutebackend.routing.graph.GraphLoaderService;
import com.mediroute.mediroutebackend.routing.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Loads the shared network graph (same Supabase data Task 1 uses) and runs
 * all three network analysis algorithms on it.
 *
 */
@Service
public class NetworkAnalysisService {

    @Autowired
    private GraphLoaderService graphLoaderService;

    private final ArticulationPointFinder articulationPointFinder = new ArticulationPointFinder();
    private final MSTBuilder mstBuilder = new MSTBuilder();
    private final CentralityCalculator centralityCalculator = new CentralityCalculator();

    public CriticalNodeResult findCriticalNodes() {
        return articulationPointFinder.findArticulationPoints(loadGraph());
    }

    public MSTResult buildBackboneNetwork() {
        return mstBuilder.buildMST(loadGraph());
    }

    public CentralityResult rankNodesByCentrality() {
        return centralityCalculator.calculateDegreeCentrality(loadGraph());
    }

    private Graph loadGraph() {
        Graph graph = graphLoaderService.loadFromDatabase();
        if (graph.nodeCount() == 0) {
            // Same fallback sample graph as Task 1's RoutingService, so
            // analysis results stay consistent across modules even before
            // Supabase is fully seeded.
            graph = buildFallbackSampleGraph();
        }
        return graph;
    }

    private Graph buildFallbackSampleGraph() {
        Graph sample = new Graph();
        sample.addNode(new Node(1L, "City Hospital", "HOSPITAL", 6.9271, 79.8612));
        sample.addNode(new Node(2L, "Junction A", "JUNCTION", 6.9310, 79.8650));
        sample.addNode(new Node(3L, "Junction B", "JUNCTION", 6.9350, 79.8700));
        sample.addNode(new Node(4L, "Ambulance Depot 1", "DEPOT", 6.9200, 79.8550));
        sample.addNode(new Node(5L, "General Hospital", "HOSPITAL", 6.9400, 79.8750));

        sample.addEdge(4L, 2L, 2.5, 4.0, true);
        sample.addEdge(2L, 1L, 1.8, 3.0, true);
        sample.addEdge(2L, 3L, 3.2, 5.0, true);
        sample.addEdge(3L, 5L, 2.0, 3.5, true);
        sample.addEdge(4L, 3L, 5.0, 8.0, true);
        return sample;
    }
}
