package com.mediroute.mediroutebackend.decision.algorithm;

import com.mediroute.mediroutebackend.decision.model.HospitalRecommendation;
import com.mediroute.mediroutebackend.decision.model.RecommendationResult;

import java.util.*;

/**
 * Two ways to select the top-k highest-scoring hospitals from a scored
 * candidate list, implemented side by side so they can be directly
 * compared. IMPORTANT: unlike Task 2's Greedy vs Knapsack DP (which can
 * genuinely disagree), these two algorithms ALWAYS produce the exact same
 * top-k set - the comparison here is purely about SPEED, not correctness
 * or optimality (see docs/07-experimental-evaluation.md).
 *
 */
public class HospitalRecommender {

    /**
     * Bounded min-heap approach: keep a heap of size k holding the current
     * top-k highest scores seen so far. Whenever a new candidate would
     * make the heap exceed size k, remove the heap's SMALLEST element
     * (the "weakest" of the current top-k) - it can never end up in the
     * final top-k, since we already have k better candidates.
     *
     * Time complexity : O(n log k) - n candidates, each heap operation
     *                    O(log k) since the heap never grows past size k.
     * Space complexity: O(k)
     */
    public RecommendationResult selectTopKUsingHeap(List<HospitalRecommendation> candidates, int k) {
        long startTime = System.nanoTime();

        PriorityQueue<HospitalRecommendation> minHeap =
                new PriorityQueue<>(Comparator.comparingDouble(HospitalRecommendation::getScore));

        for (HospitalRecommendation candidate : candidates) {
            minHeap.add(candidate);
            if (minHeap.size() > k) {
                minHeap.poll(); // discard the current weakest of the top-k
            }
        }

        List<HospitalRecommendation> ranked = new ArrayList<>(minHeap);
        ranked.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        long endTime = System.nanoTime();

        RecommendationResult result = new RecommendationResult();
        result.setRankedHospitals(ranked);
        result.setExecutionTimeNanos(endTime - startTime);
        result.setAlgorithmUsed("Bounded Min-Heap Top-K");
        return result;
    }

    /**
     * Baseline approach: sort ALL candidates fully, then take the first k.
     * Correct, simple, but does more work than necessary when k is much
     * smaller than n - included specifically to measure that "extra work"
     * empirically against the heap approach.
     *
     * Time complexity : O(n log n)
     * Space complexity: O(n)
     */
    public RecommendationResult selectTopKUsingFullSort(List<HospitalRecommendation> candidates, int k) {
        long startTime = System.nanoTime();

        List<HospitalRecommendation> sorted = new ArrayList<>(candidates);
        sorted.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        List<HospitalRecommendation> ranked = new ArrayList<>(sorted.subList(0, Math.min(k, sorted.size())));

        long endTime = System.nanoTime();

        RecommendationResult result = new RecommendationResult();
        result.setRankedHospitals(ranked);
        result.setExecutionTimeNanos(endTime - startTime);
        result.setAlgorithmUsed("Full Sort");
        return result;
    }
}
