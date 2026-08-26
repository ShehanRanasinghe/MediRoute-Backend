package com.mediroute.mediroutebackend.optimization.algorithm;

import com.mediroute.mediroutebackend.optimization.model.DispatchItem;
import com.mediroute.mediroutebackend.optimization.model.OptimizationResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classic textbook 0/1 knapsack instance (same one used in Task 2, for
 * consistency): Items (weight, value): (2,3), (3,4), (4,5); capacity 5.
 * Optimal: items 1+2 (weight 2+3=5, value 3+4=7).
 */
class KnapsackDPOptimizerTest {

    private final KnapsackDPOptimizer optimizer = new KnapsackDPOptimizer();

    @Test
    void findsOptimalValueForClassicInstance() {
        List<DispatchItem> items = List.of(
                new DispatchItem(1L, "A", 3, 2),
                new DispatchItem(2L, "B", 4, 3),
                new DispatchItem(3L, "C", 5, 4)
        );

        OptimizationResult result = optimizer.optimize(items, 5);

        assertEquals(7, result.getTotalValueAchieved());
        assertEquals(2, result.getSelectedItemIds().size());
        assertTrue(result.getSelectedItemIds().contains(1L));
        assertTrue(result.getSelectedItemIds().contains(2L));
    }

    @Test
    void zeroCapacitySelectsNothing() {
        List<DispatchItem> items = List.of(new DispatchItem(1L, "A", 9, 3));

        OptimizationResult result = optimizer.optimize(items, 0);

        assertEquals(0, result.getTotalValueAchieved());
        assertTrue(result.getSelectedItemIds().isEmpty());
    }
}
