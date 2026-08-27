package com.mediroute.mediroutebackend.allocation.benchmark;

import com.mediroute.mediroutebackend.allocation.algorithm.GreedyAllocator;
import com.mediroute.mediroutebackend.allocation.algorithm.KnapsackAllocator;
import com.mediroute.mediroutebackend.allocation.model.AllocationRequest;
import com.mediroute.mediroutebackend.allocation.model.AllocationResult;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// This benchmark compares greedy and knapsack allocation performance as the number of pending incidents grows.
// It records timing and quality differences so the project can explain why the faster method is sometimes less optimal.
public class AllocationBenchmarkRunner {

    private static final int[] REQUEST_COUNTS = {10, 50, 200, 1000};
    private static final long RANDOM_SEED = 42L;

    public static void main(String[] args) throws IOException {
        GreedyAllocator greedy = new GreedyAllocator();
        KnapsackAllocator knapsack = new KnapsackAllocator();

        try (PrintWriter writer = new PrintWriter(new FileWriter("allocation-benchmark-results.csv"))) {
            writer.println("request_count,algorithm,execution_time_ms,total_value,capacity_used,quality_gap_percent");

            for (int count : REQUEST_COUNTS) {
                List<AllocationRequest> requests = generateRandomRequests(count, RANDOM_SEED);
                int capacity = count / 4; // capacity scales with problem size, always the tighter constraint

                AllocationResult greedyResult = greedy.allocate(requests, capacity);
                AllocationResult knapsackResult = knapsack.allocate(requests, capacity);

                double qualityGap = knapsackResult.getTotalValueAchieved() == 0 ? 0 :
                        100.0 * (knapsackResult.getTotalValueAchieved() - greedyResult.getTotalValueAchieved())
                        / knapsackResult.getTotalValueAchieved();

                writer.println(count + ",Greedy," + (greedyResult.getExecutionTimeNanos() / 1_000_000.0) + ","
                        + greedyResult.getTotalValueAchieved() + "," + greedyResult.getCapacityUsed() + "," + qualityGap);

                writer.println(count + ",Knapsack DP," + (knapsackResult.getExecutionTimeNanos() / 1_000_000.0) + ","
                        + knapsackResult.getTotalValueAchieved() + "," + knapsackResult.getCapacityUsed() + ",0.0");

                System.out.println("Completed benchmark for " + count + " requests (capacity=" + capacity
                        + "). Greedy achieved " + qualityGap + "% less value than optimal.");
            }
        }

        System.out.println("Benchmark complete. Results written to allocation-benchmark-results.csv");
        System.out.println("Chart 1: execution_time_ms vs request_count (both algorithms)");
        System.out.println("Chart 2: quality_gap_percent vs request_count (Greedy only) - this is your LO3 evidence");
    }

    private static List<AllocationRequest> generateRandomRequests(int count, long seed) {
        Random random = new Random(seed);
        List<AllocationRequest> requests = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            int severity = 1 + random.nextInt(10); // 1-10
            int units = severity >= 8 ? 2 : 1;
            requests.add(new AllocationRequest(i, "SIMULATED", severity, units));
        }
        return requests;
    }
}
