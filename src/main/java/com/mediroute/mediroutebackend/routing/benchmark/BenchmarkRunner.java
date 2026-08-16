package com.mediroute.routing.benchmark;

import com.mediroute.routing.graph.AStarRouter;
import com.mediroute.routing.graph.DijkstraRouter;
import com.mediroute.routing.graph.Graph;
import com.mediroute.routing.model.RouteResult;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Standalone benchmark suite for experimental evaluation (Chapter 8).
 * Executes performance benchmarks comparing Dijkstra and A* pathfinding algorithms
 * across synthetic connected graphs of varying scale and exports results to CSV format.
 */
public class BenchmarkRunner {

    // Target node scales for evaluation
    private static final int[] GRAPH_SIZES = {50, 200, 500, 1000, 5000};
    
    // Fixed seed to ensure reproducible graph generation across test runs
    private static final long RANDOM_SEED = 42L;

    /**
     * Entry point for running the execution time benchmark.
     * Generates graphs, runs pathfinding algorithms, and outputs results to a CSV file.
     *
     * @param args Command line arguments (not used).
     * @throws IOException If writing to the CSV output file fails.
     */
    public static void main(String[] args) throws IOException {
        DijkstraRouter dijkstra = new DijkstraRouter();
        AStarRouter astar = new AStarRouter();

        // Initialize CSV report file writer
        try (PrintWriter writer = new PrintWriter(new FileWriter("benchmark-results.csv"))) {
            // Write CSV header
            writer.println("graph_size,algorithm,execution_time_ms,distance_km,path_found");

            // Evaluate algorithms against each defined graph size
            for (int size : GRAPH_SIZES) {
                // Generate a deterministic connected graph
                Graph graph = Graph.generateRandomConnected(size, size / 2, RANDOM_SEED);
                long sourceId = 1L;
                long destinationId = size;

                // Execute Dijkstra algorithm benchmark
                RouteResult dijkstraResult = dijkstra.findShortestPath(graph, sourceId, destinationId);
                writer.println(size + ",Dijkstra," 
                        + (dijkstraResult.getExecutionTimeNanos() / 1_000_000.0) + ","
                        + dijkstraResult.getTotalDistanceKm() + ","
                        + dijkstraResult.isPathFound());

                // Execute A* algorithm benchmark
                RouteResult astarResult = astar.findShortestPath(graph, sourceId, destinationId);
                writer.println(size + ",A*,"
                        + (astarResult.getExecutionTimeNanos() / 1_000_000.0) + ","
                        + astarResult.getTotalDistanceKm() + ","
                        + astarResult.isPathFound());

                // Log progress per benchmark batch
                System.out.println("Completed benchmark for graph size = " + size
                        + " (" + graph.nodeCount() + " nodes, " + graph.edgeCount() + " edges)");
            }
        }

        // Finalize execution notification
        System.out.println("Benchmark complete. Results written to benchmark-results.csv");
        System.out.println("Open this file in Excel/Sheets to chart execution_time_ms vs graph_size for both algorithms.");
    }
}