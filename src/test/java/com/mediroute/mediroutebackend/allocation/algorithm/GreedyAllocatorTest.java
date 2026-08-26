package com.mediroute.mediroutebackend.allocation.algorithm;

import com.mediroute.mediroutebackend.allocation.model.AllocationRequest;
import com.mediroute.mediroutebackend.allocation.model.AllocationResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Requests: (id, severity=value, units=weight)
 *   R1: severity 9, units 2
 *   R2: severity 5, units 1
 *   R3: severity 4, units 1
 * Capacity: 3
 *
 * Density: R1=4.5, R2=5.0, R3=4.0 -> Greedy picks R2 first (density 5.0),
 * then R1 (density 4.5, fits in remaining 2), R3 doesn't fit (remaining 0).
 * Expected: selected = [R2, R1], value = 5 + 9 = 14
 */
class GreedyAllocatorTest {

    private final GreedyAllocator allocator = new GreedyAllocator();

    @Test
    void picksHighestDensityRequestsFirst() {
        List<AllocationRequest> requests = List.of(
                new AllocationRequest(1L, "TRAUMA", 9, 2),
                new AllocationRequest(2L, "CARDIAC", 5, 1),
                new AllocationRequest(3L, "GENERAL", 4, 1)
        );

        AllocationResult result = allocator.allocate(requests, 3);

        assertEquals(14, result.getTotalValueAchieved());
        assertEquals(2, result.getSelectedIncidentIds().size());
        assertTrue(result.getSelectedIncidentIds().contains(1L));
        assertTrue(result.getSelectedIncidentIds().contains(2L));
        assertTrue(result.getUnallocatedIncidentIds().contains(3L));
    }

    @Test
    void zeroCapacityAllocatesNothing() {
        List<AllocationRequest> requests = List.of(
                new AllocationRequest(1L, "TRAUMA", 9, 2)
        );

        AllocationResult result = allocator.allocate(requests, 0);

        assertEquals(0, result.getTotalValueAchieved());
        assertTrue(result.getSelectedIncidentIds().isEmpty());
        assertEquals(1, result.getUnallocatedIncidentIds().size());
    }
}
