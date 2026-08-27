package com.mediroute.mediroutebackend.network.benchmark;

import com.mediroute.mediroutebackend.network.algorithm.ArticulationPointFinder;
import com.mediroute.mediroutebackend.network.algorithm.MSTBuilder;
import com.mediroute.mediroutebackend.network.model.CriticalNodeResult;
import com.mediroute.mediroutebackend.network.model.MSTResult;
import com.mediroute.mediroutebackend.routing.graph.Graph;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

// This benchmark compares the performance of critical-node detection and minimum spanning tree generation across larger graph sizes.
// It is used to validate that the network algorithms scale in the expected way for real-world road data.
// Confirms the theoretical O(V+E) claim for articulation point detection empirically, and shows how MST construction time grows with graph size.
public class NetworkBenchmarkRunner {

    private static final int[] GRAPH_SIZES = {50, 200, 500, 1000, 5000};
    private static final long RANDOM_SEED = 42L;

    public static void main(String[] args) throws IOException {
        ArticulationPointFinder articulationFinder = new ArticulationPointFinder();
        MSTBuilder mstBuilder = new MSTBuilder();

        try (PrintWriter writer = new PrintWriter(new FileWriter("network-benchmark-results.csv"))) {
            writer.println("graph_size,algorithm,execution_time_ms,result_count");

            for (int size : GRAPH_SIZES) {
                Graph graph = Graph.generateRandomConnected(size, size / 3, RANDOM_SEED);

                CriticalNodeResult articulationResult = articulationFinder.findArticulationPoints(graph);
                writer.println(size + ",DFS Articulation Points,"
                        + (articulationResult.getExecutionTimeNanos() / 1_000_000.0) + ","
                        + articulationResult.getCriticalNodeIds().size());

                MSTResult mstResult = mstBuilder.buildMST(graph);
                writer.println(size + ",Prim's MST,"
                        + (mstResult.getExecutionTimeNanos() / 1_000_000.0) + ","
                        + mstResult.getEdges().size());

                System.out.println("Completed size=" + size + ": "
                        + articulationResult.getCriticalNodeIds().size() + " critical node(s), MST has "
                        + mstResult.getEdges().size() + " edge(s)");
            }
        }

        System.out.println("Benchmark complete. Results written to network-benchmark-results.csv");
        System.out.println("Chart: execution_time_ms vs graph_size, one line per algorithm - both should trend linearly (O(V+E)), confirming the theoretical complexity.");
    }
}
