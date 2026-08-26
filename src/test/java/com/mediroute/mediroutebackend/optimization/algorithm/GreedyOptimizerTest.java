package com.mediroute.mediroutebackend.optimization.algorithm;

import com.mediroute.mediroutebackend.optimization.model.DispatchItem;
import com.mediroute.mediroutebackend.optimization.model.OptimizationResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GreedyOptimizerTest {

    private final GreedyOptimizer greedy = new GreedyOptimizer();
    private final KnapsackDPOptimizer dp = new KnapsackDPOptimizer();

    @Test
    void greedyCanBeSuboptimalComparedToExactOptimizers() {
        // Item 1: value 10, weight 3 (density 3.33)
        // Items 2+3: value 6 each, weight 2 each (density 3.0 each)
        // Capacity 4: Greedy takes item 1 alone (value 10, density-first).
        // Optimal (DP/Backtracking) takes items 2+3 instead (value 12).
        List<DispatchItem> items = List.of(
                new DispatchItem(1L, "A", 10, 3),
                new DispatchItem(2L, "B", 6, 2),
                new DispatchItem(3L, "C", 6, 2)
        );

        OptimizationResult greedyResult = greedy.optimize(items, 4);
        OptimizationResult dpResult = dp.optimize(items, 4);

        assertEquals(10, greedyResult.getTotalValueAchieved());
        assertEquals(12, dpResult.getTotalValueAchieved());
        assertTrue(dpResult.getTotalValueAchieved() > greedyResult.getTotalValueAchieved(),
                "This is a deliberate counterexample proving Greedy is an approximation, not always optimal");
    }

    @Test
    void zeroCapacitySelectsNothing() {
        List<DispatchItem> items = List.of(new DispatchItem(1L, "A", 9, 3));

        OptimizationResult result = greedy.optimize(items, 0);

        assertEquals(0, result.getTotalValueAchieved());
        assertTrue(result.getSelectedItemIds().isEmpty());
    }
}
