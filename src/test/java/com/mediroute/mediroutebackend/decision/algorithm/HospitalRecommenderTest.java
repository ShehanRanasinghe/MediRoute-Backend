package com.mediroute.mediroutebackend.decision.algorithm;

import com.mediroute.mediroutebackend.decision.model.HospitalRecommendation;
import com.mediroute.mediroutebackend.decision.model.RecommendationResult;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Owner: Manura
 *
 * Candidates with scores: 1->0.9, 2->0.3, 3->0.7, 4->0.5, 5->0.1
 * Top 3 by score: [1 (0.9), 3 (0.7), 4 (0.5)]
 */
class HospitalRecommenderTest {

    private final HospitalRecommender recommender = new HospitalRecommender();

    @Test
    void heapAndFullSortProduceIdenticalTopKInSameOrder() {
        List<HospitalRecommendation> candidates = List.of(
                candidate(1L, 0.9), candidate(2L, 0.3), candidate(3L, 0.7),
                candidate(4L, 0.5), candidate(5L, 0.1)
        );

        RecommendationResult heapResult = recommender.selectTopKUsingHeap(candidates, 3);
        RecommendationResult sortResult = recommender.selectTopKUsingFullSort(candidates, 3);

        List<Long> heapIds = extractIds(heapResult);
        List<Long> sortIds = extractIds(sortResult);

        // Both are EXACT algorithms - they must always agree, unlike Task 2's
        // Greedy vs Knapsack DP which can genuinely diverge.
        assertEquals(sortIds, heapIds);
        assertEquals(List.of(1L, 3L, 4L), heapIds);
    }

    @Test
    void kLargerThanCandidateCountReturnsAllCandidates() {
        List<HospitalRecommendation> candidates = List.of(candidate(1L, 0.5), candidate(2L, 0.8));

        RecommendationResult result = recommender.selectTopKUsingHeap(candidates, 5);

        assertEquals(2, result.getRankedHospitals().size());
    }

    private List<Long> extractIds(RecommendationResult result) {
        return result.getRankedHospitals().stream()
                .map(HospitalRecommendation::getHospitalId)
                .collect(Collectors.toList());
    }

    private HospitalRecommendation candidate(Long id, double score) {
        HospitalRecommendation c = new HospitalRecommendation();
        c.setHospitalId(id);
        c.setScore(score);
        return c;
    }
}
