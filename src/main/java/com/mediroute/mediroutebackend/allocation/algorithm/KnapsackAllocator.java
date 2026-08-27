package com.mediroute.mediroutebackend.allocation.algorithm;

import com.mediroute.mediroutebackend.allocation.model.AllocationRequest;
import com.mediroute.mediroutebackend.allocation.model.AllocationResult;

import java.util.*;


// This allocator solves the emergency allocation problem with dynamic programming so it can choose the best set of incidents under a limited resource capacity.
// It is the optimal comparison point for the faster greedy approach and is used when the system needs the strongest possible allocation result.
public class KnapsackAllocator {

    public AllocationResult allocate(List<AllocationRequest> requests, int totalCapacity) {
        long startTime = System.nanoTime();

        int n = requests.size();
        int[][] dp = new int[n + 1][totalCapacity + 1];

        for (int i = 1; i <= n; i++) {
            AllocationRequest request = requests.get(i - 1);
            for (int capacity = 0; capacity <= totalCapacity; capacity++) {
                dp[i][capacity] = dp[i - 1][capacity]; // default: skip this request

                if (request.getResourceUnitsNeeded() <= capacity) {
                    int takeValue = dp[i - 1][capacity - request.getResourceUnitsNeeded()] + request.getSeverityScore();
                    if (takeValue > dp[i][capacity]) {
                        dp[i][capacity] = takeValue;
                    }
                }
            }
        }

        // Backtrack through the table to find which requests were actually selected
        List<Long> selected = new ArrayList<>();
        List<Long> unallocated = new ArrayList<>();
        int remainingCapacity = totalCapacity;

        for (int i = n; i >= 1; i--) {
            AllocationRequest request = requests.get(i - 1);
            boolean wasTaken = dp[i][remainingCapacity] != dp[i - 1][remainingCapacity];

            if (wasTaken) {
                selected.add(request.getIncidentId());
                remainingCapacity -= request.getResourceUnitsNeeded();
            } else {
                unallocated.add(request.getIncidentId());
            }
        }

        long endTime = System.nanoTime();

        AllocationResult result = new AllocationResult();
        result.setAlgorithmUsed("Knapsack DP");
        result.setSelectedIncidentIds(selected);
        result.setUnallocatedIncidentIds(unallocated);
        result.setTotalValueAchieved(dp[n][totalCapacity]);
        result.setTotalCapacity(totalCapacity);
        result.setCapacityUsed(totalCapacity - remainingCapacity);
        result.setExecutionTimeNanos(endTime - startTime);
        return result;
    }
}
