package com.mediroute.mediroutebackend.allocation.algorithm;

import com.mediroute.mediroutebackend.allocation.model.AllocationRequest;
import com.mediroute.mediroutebackend.allocation.model.AllocationResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * GreedyAllocator distributes a limited number of resource units among
 * patient incidents.
 *
 * Each request has:
 * - A severity score, which represents its priority value.
 * - A required number of resource units, which represents its cost.
 *
 * The algorithm calculates the density of each request:
 *
 * density = severity score / resource units needed
 *
 * Requests with a higher density are considered first. A request is selected
 * only when it fits within the remaining capacity.
 *
 * A maximum heap is implemented using PriorityQueue to efficiently retrieve
 * the next highest-priority request.
 *
 * Greedy allocation is fast and suitable for real-time decision-making.
 * However, it does not always produce the optimal result for the 0/1
 * allocation problem. The Knapsack algorithm is used to find and compare
 * the optimal solution.
 *
 * Time complexity: O(n log n)
 * Space complexity: O(n)
 *
 */
public final class GreedyAllocator {

    private static final String ALGORITHM_NAME = "Greedy";

    /**
     * Allocates available resource capacity among patient requests.
     *
     * @param requests patient requests considered for allocation
     * @param totalCapacity total number of available resource units
     * @return result containing selected incidents, unallocated incidents
     *         and allocation totals
     */
    public AllocationResult allocate(
            List<AllocationRequest> requests,
            int totalCapacity
    ) {
        // Validate the data before beginning the allocation.
        validateInputs(requests, totalCapacity);

        // Start measuring only the algorithm execution time.
        long startTime = System.nanoTime();

        // Create a maximum heap that places the best request first.
        PriorityQueue<AllocationRequest> requestQueue =
                new PriorityQueue<>(createRequestComparator());

        // Copy requests into the queue without changing the original list.
        requestQueue.addAll(requests);

        // Store the allocation decision for each incident.
        List<Long> selectedIncidentIds = new ArrayList<>();
        List<Long> unallocatedIncidentIds = new ArrayList<>();

        // Track the remaining capacity and achieved priority value.
        int remainingCapacity = totalCapacity;
        int totalValueAchieved = 0;

        // Process requests from the highest density to the lowest density.
        while (!requestQueue.isEmpty()) {
            AllocationRequest request = requestQueue.poll();

            if (canAllocate(request, remainingCapacity)) {
                // Select the request because it fits within the capacity.
                selectedIncidentIds.add(request.getIncidentId());
                remainingCapacity -= request.getResourceUnitsNeeded();
                totalValueAchieved += request.getSeverityScore();
            } else {
                // Record the request that cannot fit within the capacity.
                unallocatedIncidentIds.add(request.getIncidentId());
            }
        }

        // Stop the execution-time measurement.
        long executionTimeNanos = System.nanoTime() - startTime;

        // Convert the allocation decision into the common result model.
        return createResult(
                selectedIncidentIds,
                unallocatedIncidentIds,
                totalValueAchieved,
                totalCapacity,
                remainingCapacity,
                executionTimeNanos
        );
    }

    // This helper checks whether the request fits before it is accepted into the allocation.
    private boolean canAllocate(
            AllocationRequest request,
            int remainingCapacity
    ) {
        return request.getResourceUnitsNeeded() <= remainingCapacity;
    }

    /**
     * Defines the order used by the maximum heap.
     *
     * Requests are ordered by:
     * 1. Higher density
     * 2. Higher severity
     * 3. Lower resource requirement
     * 4. Lower incident ID
     *
     * The additional comparisons ensure predictable results when requests
     * have the same density.
     */
    private Comparator<AllocationRequest> createRequestComparator() {
        return Comparator
                .comparingDouble(this::calculateDensity)
                .reversed()
                .thenComparing(
                        Comparator.comparingInt(
                                AllocationRequest::getSeverityScore
                        ).reversed()
                )
                .thenComparingInt(
                        AllocationRequest::getResourceUnitsNeeded
                )
                .thenComparingLong(
                        AllocationRequest::getIncidentId
                );
    }

    //Calculates the priority value received from one resource unit.
    private double calculateDensity(AllocationRequest request) {
        return (double) request.getSeverityScore()
                / request.getResourceUnitsNeeded();
    }

    /**
     * Creates the result returned by the allocator.
     *
     * Keeping result creation in a separate method makes the main allocation
     * method easier to read.
     */
    private AllocationResult createResult(
            List<Long> selectedIncidentIds,
            List<Long> unallocatedIncidentIds,
            int totalValueAchieved,
            int totalCapacity,
            int remainingCapacity,
            long executionTimeNanos
    ) {
        AllocationResult result = new AllocationResult();

        result.setAlgorithmUsed(ALGORITHM_NAME);
        result.setSelectedIncidentIds(selectedIncidentIds);
        result.setUnallocatedIncidentIds(unallocatedIncidentIds);
        result.setTotalValueAchieved(totalValueAchieved);
        result.setTotalCapacity(totalCapacity);
        result.setCapacityUsed(totalCapacity - remainingCapacity);
        result.setExecutionTimeNanos(executionTimeNanos);

        return result;
    }

    /**
     * Checks whether the supplied allocation data is valid.
     *
     * Invalid data is rejected before the algorithm starts so that it cannot
     * produce incorrect results or unexpected errors.
     */
    private void validateInputs(
            List<AllocationRequest> requests,
            int totalCapacity
    ) {
        Objects.requireNonNull(
                requests,
                "requests must not be null"
        );

        if (totalCapacity < 0) {
            throw new IllegalArgumentException(
                    "totalCapacity must not be negative"
            );
        }

        // Used to prevent the same incident from being processed twice.
        Set<Long> incidentIds = new HashSet<>();

        for (AllocationRequest request : requests) {
            validateRequest(request, incidentIds);
        }
    }

    // This helper checks a single request for missing values or duplicate incident entries.
    private void validateRequest(
            AllocationRequest request,
            Set<Long> incidentIds
    ) {
        Objects.requireNonNull(
                request,
                "requests must not contain null elements"
        );

        if (request.getIncidentId() == null) {
            throw new IllegalArgumentException(
                    "incidentId must not be null"
            );
        }

        if (!incidentIds.add(request.getIncidentId())) {
            throw new IllegalArgumentException(
                    "duplicate incidentId: " + request.getIncidentId()
            );
        }

        if (request.getResourceUnitsNeeded() <= 0) {
            throw new IllegalArgumentException(
                    "resourceUnitsNeeded must be greater than zero"
            );
        }

        if (request.getSeverityScore() < 0) {
            throw new IllegalArgumentException(
                    "severityScore must not be negative"
            );
        }
    }
}