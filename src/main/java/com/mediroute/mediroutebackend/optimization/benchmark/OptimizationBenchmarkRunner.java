package com.mediroute.mediroutebackend.optimization.benchmark;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mediroute.mediroutebackend.optimization.algorithm.BacktrackingOptimizer;
import com.mediroute.mediroutebackend.optimization.algorithm.GreedyOptimizer;
import com.mediroute.mediroutebackend.optimization.algorithm.KnapsackDPOptimizer;
import com.mediroute.mediroutebackend.routing.model.DispatchItem;
import com.mediroute.mediroutebackend.routing.model.OptimizationResult;

public class OptimizationBenchmarkRunner {

    private static final int[] SMALL_SIZES = {10, 15, 20, 25};
    private static final int[] LARGE_SIZES = {50, 200, 1000, 5000};
    private static final long RANDOM_SEED = 42L;

    public static void main(String[] args) throws IOException {
        KnapsackDPOptimizer dp = new KnapsackDPOptimizer();
        GreedyOptimizer greedy = new GreedyOptimizer();
        BacktrackingOptimizer backtracking = new BacktrackingOptimizer();

        try (PrintWriter writer = new PrintWriter(new FileWriter("optimization-benchmark-small.csv"))) {
            writer.println("item_count,algorithm,execution_time_ms,total_value,capacity_used");

            for (int size : SMALL_SIZES) {
                List<DispatchItem> items = generateRandomItems(size, RANDOM_SEED);
                int capacity = size * 3;

                OptimizationResult dpResult = dp.optimize(items, capacity);
                writer.println(size + ",Dynamic Programming," + (dpResult.getExecutionTimeNanos() / 1_000_000.0)
                        + "," + dpResult.getTotalValueAchieved() + "," + dpResult.getCapacityUsed());

                OptimizationResult greedyResult = greedy.optimize(items, capacity);
                writer.println(size + ",Greedy," + (greedyResult.getExecutionTimeNanos() / 1_000_000.0)
                        + "," + greedyResult.getTotalValueAchieved() + "," + greedyResult.getCapacityUsed());

                OptimizationResult backtrackingResult = backtracking.optimize(items, capacity);
                writer.println(size + ",Backtracking," + (backtrackingResult.getExecutionTimeNanos() / 1_000_000.0)
                        + "," + backtrackingResult.getTotalValueAchieved() + "," + backtrackingResult.getCapacityUsed());

                System.out.println("Small benchmark size=" + size
                        + ": DP value=" + dpResult.getTotalValueAchieved()
                        + ", Backtracking value=" + backtrackingResult.getTotalValueAchieved()
                        + " (should always match - both are exact)"
                        + ", Greedy value=" + greedyResult.getTotalValueAchieved());
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("optimization-benchmark-large.csv"))) {
            writer.println("item_count,algorithm,execution_time_ms,total_value,capacity_used");

            for (int size : LARGE_SIZES) {
                List<DispatchItem> items = generateRandomItems(size, RANDOM_SEED);
                int capacity = size * 3;

                OptimizationResult dpResult = dp.optimize(items, capacity);
                writer.println(size + ",Dynamic Programming," + (dpResult.getExecutionTimeNanos() / 1_000_000.0)
                        + "," + dpResult.getTotalValueAchieved() + "," + dpResult.getCapacityUsed());

                OptimizationResult greedyResult = greedy.optimize(items, capacity);
                writer.println(size + ",Greedy," + (greedyResult.getExecutionTimeNanos() / 1_000_000.0)
                        + "," + greedyResult.getTotalValueAchieved() + "," + greedyResult.getCapacityUsed());

                System.out.println("Large benchmark size=" + size + " done (Backtracking intentionally skipped here).");
            }
        }

        System.out.println("Done.");
        System.out.println("optimization-benchmark-small.csv: all 3 algorithms, safe sizes for Backtracking.");
        System.out.println("optimization-benchmark-large.csv: DP vs Greedy only, showing DP's own capacity-driven scaling limit.");
    }

    private static List<DispatchItem> generateRandomItems(int count, long seed) {
        Random random = new Random(seed);
        List<DispatchItem> items = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            int value = 1 + random.nextInt(20);
            int weight = 1 + random.nextInt(10);
            items.add(new DispatchItem(i, "Item" + i, value, weight));
        }
        return items;
    }
}
