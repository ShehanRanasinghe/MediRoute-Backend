package com.mediroute.mediroutebackend.optimization.algorithm;

import com.mediroute.mediroutebackend.optimization.model.DispatchItem;
import com.mediroute.mediroutebackend.optimization.model.OptimizationResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Confirms Backtracking is genuinely EXACT by cross-checking it against
 * KnapsackDPOptimizer - both must always agree, since both compute the
 * true optimal value, just via different search strategies.
 */
class BacktrackingOptimizerTest {

    private final BacktrackingOptimizer backtracking = new BacktrackingOptimizer();
    private final KnapsackDPOptimizer dp = new KnapsackDPOptimizer();

    @Test
    void findsSameOptimalValueAsDPOnClassicInstance() {
        List<DispatchItem> items = List.of(
                new DispatchItem(1L, "A", 3, 2),
                new DispatchItem(2L, "B", 4, 3),
                new DispatchItem(3L, "C", 5, 4)
        );

        OptimizationResult result = backtracking.optimize(items, 5);

        assertEquals(7, result.getTotalValueAchieved());
    }

    @Test
    void matchesDPExactlyAcrossSeveralRandomInstances() {
        Random random = new Random(7L);

        for (int trial = 0; trial < 10; trial++) {
            List<DispatchItem> items = new ArrayList<>();
            int itemCount = 5 + random.nextInt(8); // small enough to stay fast
            for (long i = 1; i <= itemCount; i++) {
                items.add(new DispatchItem(i, "Item" + i, 1 + random.nextInt(15), 1 + random.nextInt(8)));
            }
            int capacity = 10 + random.nextInt(20);

            OptimizationResult dpResult = dp.optimize(items, capacity);
            OptimizationResult backtrackingResult = backtracking.optimize(items, capacity);

            assertEquals(dpResult.getTotalValueAchieved(), backtrackingResult.getTotalValueAchieved(),
                    "DP and Backtracking must always find the same optimal value (trial " + trial + ")");
        }
    }
}
