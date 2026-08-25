package com.mediroute.mediroutebackend.optimization.service;

import com.mediroute.mediroutebackend.common.models.SupplyItem;
import com.mediroute.mediroutebackend.common.models.SupplyItemStatus;
import com.mediroute.mediroutebackend.common.models.repository.SupplyItemRepository;
import com.mediroute.mediroutebackend.optimization.algorithm.BacktrackingOptimizer;
import com.mediroute.mediroutebackend.optimization.algorithm.GreedyOptimizer;
import com.mediroute.mediroutebackend.optimization.algorithm.KnapsackDPOptimizer;
import com.mediroute.mediroutebackend.optimization.model.DispatchItem;
import com.mediroute.mediroutebackend.optimization.model.OptimizationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class OptimizationService {

    @Autowired
    private SupplyItemRepository supplyItemRepository;

    private final KnapsackDPOptimizer dpOptimizer = new KnapsackDPOptimizer();
    private final GreedyOptimizer greedyOptimizer = new GreedyOptimizer();
    private final BacktrackingOptimizer backtrackingOptimizer = new BacktrackingOptimizer();

    public OptimizationResult optimize(int vehicleCapacity, String algorithm) {
        List<DispatchItem> items = loadPendingItems();
        String choice = algorithm == null ? "dp" : algorithm.toLowerCase();

        return switch (choice) {
            case "greedy" -> greedyOptimizer.optimize(items, vehicleCapacity);
            case "backtracking" -> backtrackingOptimizer.optimize(items, vehicleCapacity);
            default -> dpOptimizer.optimize(items, vehicleCapacity);
        };
    }

    public Map<String, OptimizationResult> compareAll(int vehicleCapacity) {
        List<DispatchItem> items = loadPendingItems();

        Map<String, OptimizationResult> results = new HashMap<>();
        results.put("dp", dpOptimizer.optimize(items, vehicleCapacity));
        results.put("greedy", greedyOptimizer.optimize(items, vehicleCapacity));

        // Safety cutoff: Backtracking's worst case is O(2^n). Only run it
        // for reasonably small pending-item counts so a busy day with many
        // pending items can't accidentally trigger a runaway computation.
        if (items.size() <= 25) {
            results.put("backtracking", backtrackingOptimizer.optimize(items, vehicleCapacity));
        }

        return results;
    }

    public List<SupplyItem> getPendingItems() {
        return supplyItemRepository.findByStatus(SupplyItemStatus.PENDING);
    }

    private List<DispatchItem> loadPendingItems() {
        return supplyItemRepository.findByStatus(SupplyItemStatus.PENDING).stream()
                .map(item -> new DispatchItem(item.getId(), item.getItemName(), item.getUrgencyValue(), item.getSizeCost()))
                .collect(Collectors.toList());
    }
}
