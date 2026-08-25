package com.mediroute.mediroutebackend.decision.benchmark;

import com.mediroute.mediroutebackend.decision.algorithm.HospitalRecommender;
import com.mediroute.mediroutebackend.decision.model.HospitalRecommendation;
import com.mediroute.mediroutebackend.decision.model.RecommendationResult;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Standalone benchmark for the experimental evaluation chapter (Chapter 8).
 * NOT a Spring bean - run directly (right-click -> Run in IntelliJ).
 *
 * Compares the bounded min-heap approach against full-sort as candidate
 * count (n) grows, with k fixed and small (k=5) - this is exactly the
 * scenario where O(n log k) should visibly outperform O(n log n).
 *
 */
public class RecommendationBenchmarkRunner {

    private static final int[] CANDIDATE_COUNTS = {50, 200, 1000, 5000, 20000};
    private static final int K = 5;
    private static final long RANDOM_SEED = 42L;

    public static void main(String[] args) throws IOException {
        HospitalRecommender recommender = new HospitalRecommender();

        try (PrintWriter writer = new PrintWriter(new FileWriter("recommendation-benchmark-results.csv"))) {
            writer.println("candidate_count,algorithm,execution_time_ms,k");

            for (int count : CANDIDATE_COUNTS) {
                List<HospitalRecommendation> candidates = generateRandomCandidates(count, RANDOM_SEED);

                RecommendationResult heapResult = recommender.selectTopKUsingHeap(candidates, K);
                writer.println(count + ",Bounded Min-Heap Top-K,"
                        + (heapResult.getExecutionTimeNanos() / 1_000_000.0) + "," + K);

                RecommendationResult sortResult = recommender.selectTopKUsingFullSort(candidates, K);
                writer.println(count + ",Full Sort,"
                        + (sortResult.getExecutionTimeNanos() / 1_000_000.0) + "," + K);

                System.out.println("Completed candidate_count=" + count);
            }
        }

        System.out.println("Benchmark complete. Results written to recommendation-benchmark-results.csv");
        System.out.println("Chart: execution_time_ms vs candidate_count, one line per algorithm - the gap should widen as candidate_count grows, since k stays fixed at " + K + ".");
    }

    private static List<HospitalRecommendation> generateRandomCandidates(int count, long seed) {
        Random random = new Random(seed);
        List<HospitalRecommendation> list = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            HospitalRecommendation candidate = new HospitalRecommendation();
            candidate.setHospitalId(i);
            candidate.setHospitalName("Hospital" + i);
            candidate.setScore(random.nextDouble());
            candidate.setDistanceKm(random.nextDouble() * 20);
            candidate.setSpecialtyMatch(random.nextBoolean());
            candidate.setAvailableBeds(random.nextInt(50));
            list.add(candidate);
        }
        return list;
    }
}
