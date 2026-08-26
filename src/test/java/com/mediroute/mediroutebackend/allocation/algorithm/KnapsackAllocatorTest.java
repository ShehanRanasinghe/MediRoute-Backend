package com.mediroute.mediroutebackend.allocation.algorithm;

import com.mediroute.mediroutebackend.allocation.model.AllocationRequest;
import com.mediroute.mediroutebackend.allocation.model.AllocationResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KnapsackAllocatorTest {

    private final KnapsackAllocator allocator = new KnapsackAllocator();

    @Test
    void findsOptimalCombinationForClassicKnapsackInstance() {
        List<AllocationRequest> requests = List.of(
                new AllocationRequest(1L, "A", 3, 2),
                new AllocationRequest(2L, "B", 4, 3),
                new AllocationRequest(3L, "C", 5, 4)
        );

        AllocationResult result = allocator.allocate(requests, 5);

        assertEquals(7, result.getTotalValueAchieved());
        assertEquals(2, result.getSelectedIncidentIds().size());
        assertTrue(result.getSelectedIncidentIds().contains(1L));
        assertTrue(result.getSelectedIncidentIds().contains(2L));
    }

    @Test
    void knapsackNeverPerformsWorseThanGreedyOnSameInput() {

        List<AllocationRequest> requests = List.of(
                new AllocationRequest(1L, "A", 10, 3),
                new AllocationRequest(2L, "B", 6, 2),
                new AllocationRequest(3L, "C", 6, 2)
        );

        AllocationResult knapsackResult = allocator.allocate(requests, 4);
        AllocationResult greedyResult = new GreedyAllocator().allocate(requests, 4);

        assertEquals(12, knapsackResult.getTotalValueAchieved());
        assertTrue(knapsackResult.getTotalValueAchieved() >= greedyResult.getTotalValueAchieved(),
                "Knapsack DP must never find a worse total value than Greedy on the same input");
    }

    @Test
    void zeroCapacityAllocatesNothing() {
        List<AllocationRequest> requests = List.of(new AllocationRequest(1L, "A", 9, 2));
        AllocationResult result = allocator.allocate(requests, 0);

        assertEquals(0, result.getTotalValueAchieved());
        assertTrue(result.getSelectedIncidentIds().isEmpty());
    }
}
