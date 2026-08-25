package com.mediroute.mediroutebackend.optimization.algorithm;

import java.util.*;
import com.mediroute.mediroutebackend.optimization.model.DispatchItem;
import com.mediroute.mediroutebackend.optimization.model.OptimizationResult;


public class GreedyOptimizer {

    public OptimizationResult optimize(List<DispatchItem> items, int capacity) {
        long startTime = System.nanoTime();

        PriorityQueue<DispatchItem> maxHeap =
                new PriorityQueue<>((a, b) -> Double.compare(b.densityScore(), a.densityScore()));
        maxHeap.addAll(items);

        List<Long> selected = new ArrayList<>();
        List<Long> unselected = new ArrayList<>();
        int remaining = capacity;
        int totalValue = 0;

        while (!maxHeap.isEmpty()) {
            DispatchItem item = maxHeap.poll();
            if (item.getWeight() <= remaining) {
                selected.add(item.getId());
                remaining -= item.getWeight();
                totalValue += item.getValue();
            } else {
                unselected.add(item.getId());
            }
        }

        long endTime = System.nanoTime();

        OptimizationResult result = new OptimizationResult();
        result.setSelectedItemIds(selected);
        result.setUnselectedItemIds(unselected);
        result.setTotalValueAchieved(totalValue);
        result.setTotalCapacity(capacity);
        result.setCapacityUsed(capacity - remaining);
        result.setExecutionTimeNanos(endTime - startTime);
        result.setAlgorithmUsed("Greedy (Value Density)");
        return result;
    }
}
