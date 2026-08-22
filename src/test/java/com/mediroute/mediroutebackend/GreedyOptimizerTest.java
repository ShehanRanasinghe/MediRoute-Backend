package com.mediroute.mediroutebackend;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.mediroute.mediroutebackend.optimization.algorithm.GreedyOptimizer;
import com.mediroute.mediroutebackend.optimization.algorithm.KnapsackDPOptimizer;
import com.mediroute.mediroutebackend.routing.model.DispatchItem;
import com.mediroute.mediroutebackend.routing.model.OptimizationResult;


class GreedyOptimizerTest {

    private final GreedyOptimizer greedy = new GreedyOptimizer();
    private final KnapsackDPOptimizer dp = new KnapsackDPOptimizer();

    @Test
    void greedyCanBeSuboptimalComparedToExactOptimizers() {
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
