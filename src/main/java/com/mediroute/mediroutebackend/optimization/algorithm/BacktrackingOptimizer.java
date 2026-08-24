package com.mediroute.mediroutebackend.optimization.algorithm;

import com.mediroute.mediroutebackend.routing.model.DispatchItem;
import com.mediroute.mediroutebackend.routing.model.OptimizationResult;

import java.util.*;
import java.util.stream.Collectors;

public class BacktrackingOptimizer {

    private int bestValue;
    private List<Long> bestSelection;

    public OptimizationResult optimize(List<DispatchItem> items, int capacity) {
        long startTime = System.nanoTime();

        List<DispatchItem> sortedByDensity = new ArrayList<>(items);
        sortedByDensity.sort((a, b) -> Double.compare(b.densityScore(), a.densityScore()));

        bestValue = 0;
        bestSelection = new ArrayList<>();

        branchAndBound(sortedByDensity, 0, capacity, 0, new ArrayList<>());

        long endTime = System.nanoTime();

        List<Long> selected = bestSelection;
        Set<Long> selectedSet = new HashSet<>(selected);
        List<Long> unselected = items.stream()
                .map(DispatchItem::getId)
                .filter(id -> !selectedSet.contains(id))
                .collect(Collectors.toList());

        int usedCapacity = items.stream()
                .filter(item -> selectedSet.contains(item.getId()))
                .mapToInt(DispatchItem::getWeight)
                .sum();

        OptimizationResult result = new OptimizationResult();
        result.setSelectedItemIds(selected);
        result.setUnselectedItemIds(unselected);
        result.setTotalValueAchieved(bestValue);
        result.setTotalCapacity(capacity);
        result.setCapacityUsed(usedCapacity);
        result.setExecutionTimeNanos(endTime - startTime);
        result.setAlgorithmUsed("Backtracking (Branch and Bound)");
        return result;
    }

    private void branchAndBound(List<DispatchItem> items, int index, int remainingCapacity,
                                 int currentValue, List<Long> currentSelection) {

        if (currentValue > bestValue) {
            bestValue = currentValue;
            bestSelection = new ArrayList<>(currentSelection);
        }

        if (index == items.size()) {
            return;
        }

        int optimisticBound = currentValue + fractionalUpperBound(items, index, remainingCapacity);
        if (optimisticBound <= bestValue) {
            return;
        }

        DispatchItem item = items.get(index);

        if (item.getWeight() <= remainingCapacity) {
            currentSelection.add(item.getId());
            branchAndBound(items, index + 1, remainingCapacity - item.getWeight(),
                    currentValue + item.getValue(), currentSelection);
            currentSelection.remove(currentSelection.size() - 1);
        }

        branchAndBound(items, index + 1, remainingCapacity, currentValue, currentSelection);
    }

    private int fractionalUpperBound(List<DispatchItem> items, int startIndex, int remainingCapacity) {
        double bound = 0;
        int capacity = remainingCapacity;

        for (int i = startIndex; i < items.size() && capacity > 0; i++) {
            DispatchItem item = items.get(i);
            if (item.getWeight() <= capacity) {
                bound += item.getValue();
                capacity -= item.getWeight();
            } else {
                bound += item.densityScore() * capacity;
                capacity = 0;
            }
        }
        return (int) bound;
    }
}