package com.mediroute.mediroutebackend.optimization.algorithm;

import com.mediroute.mediroutebackend.optimization.model.DispatchItem;
import com.mediroute.mediroutebackend.optimization.model.OptimizationResult;

import java.util.*;

// This optimizer uses dynamic programming to find the strongest valid item set without trying every possible combination.
// It is the standard optimal approach for the 0/1 knapsack problem and is used as the main comparison target.
public class KnapsackDPOptimizer {

    public OptimizationResult optimize(List<DispatchItem> items, int capacity) {
        long startTime = System.nanoTime();

        int n = items.size();
        int[][] dp = new int[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            DispatchItem item = items.get(i - 1);
            for (int c = 0; c <= capacity; c++) {
                dp[i][c] = dp[i - 1][c]; 

                if (item.getWeight() <= c) {
                    int takeValue = dp[i - 1][c - item.getWeight()] + item.getValue();
                    if (takeValue > dp[i][c]) {
                        dp[i][c] = takeValue;
                    }
                }
            }
        }

        List<Long> selected = new ArrayList<>();
        List<Long> unselected = new ArrayList<>();
        int remaining = capacity;

        for (int i = n; i >= 1; i--) {
            DispatchItem item = items.get(i - 1);
            boolean wasTaken = dp[i][remaining] != dp[i - 1][remaining];

            if (wasTaken) {
                selected.add(item.getId());
                remaining -= item.getWeight();
            } else {
                unselected.add(item.getId());
            }
        }

        long endTime = System.nanoTime();

        OptimizationResult result = new OptimizationResult();
        result.setSelectedItemIds(selected);
        result.setUnselectedItemIds(unselected);
        result.setTotalValueAchieved(dp[n][capacity]);
        result.setTotalCapacity(capacity);
        result.setCapacityUsed(capacity - remaining);
        result.setExecutionTimeNanos(endTime - startTime);
        result.setAlgorithmUsed("Dynamic Programming (0/1 Knapsack)");
        return result;
    }
}
