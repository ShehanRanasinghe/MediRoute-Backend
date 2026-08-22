package com.mediroute.mediroutebackend.allocation.algorithm;

import com.mediroute.mediroutebackend.allocation.model.AllocationRequest;
import com.mediroute.mediroutebackend.allocation.model.AllocationResult;

import java.util.*;

/**
 * Greedy allocation: always assign the next-highest value-to-weight
 * ("density") request that still fits in the remaining capacity.
 *
 * WHY A MAX-HEAP (PriorityQueue):
 * We repeatedly need "the highest-density request not yet considered".
 * A max-heap gives O(log n) insert and O(log n) extract-max, instead of
 * re-sorting the remaining list after every decision.
 *
 * Time complexity : O(n log n)  (n = number of pending requests)
 * Space complexity: O(n)
 *
 * NOT guaranteed optimal for the 0/1 case (see KnapsackAllocator for the
 * exact comparison) - but very fast, which matters for real-time dispatch.
 */
public class GreedyAllocator {

    public AllocationResult allocate(List<AllocationRequest> requests, int totalCapacity) {
        long startTime = System.nanoTime();

        PriorityQueue<AllocationRequest> maxHeap =
                new PriorityQueue<>((a, b) -> Double.compare(b.densityScore(), a.densityScore()));
        maxHeap.addAll(requests);

        List<Long> selected = new ArrayList<>();
        List<Long> unallocated = new ArrayList<>();
        int remainingCapacity = totalCapacity;
        int totalValue = 0;

        while (!maxHeap.isEmpty()) {
            AllocationRequest request = maxHeap.poll();
            if (request.getResourceUnitsNeeded() <= remainingCapacity) {
                selected.add(request.getIncidentId());
                remainingCapacity -= request.getResourceUnitsNeeded();
                totalValue += request.getSeverityScore();
            } else {
                unallocated.add(request.getIncidentId());
            }
        }

        long endTime = System.nanoTime();

        AllocationResult result = new AllocationResult();
        result.setAlgorithmUsed("Greedy");
        result.setSelectedIncidentIds(selected);
        result.setUnallocatedIncidentIds(unallocated);
        result.setTotalValueAchieved(totalValue);
        result.setTotalCapacity(totalCapacity);
        result.setCapacityUsed(totalCapacity - remainingCapacity);
        result.setExecutionTimeNanos(endTime - startTime);
        return result;
    }
}
