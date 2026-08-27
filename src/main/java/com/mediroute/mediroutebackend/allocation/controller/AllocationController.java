package com.mediroute.mediroutebackend.allocation.controller;

import com.mediroute.mediroutebackend.allocation.model.AllocationRequestDTO;
import com.mediroute.mediroutebackend.allocation.model.AllocationResult;
import com.mediroute.mediroutebackend.allocation.service.AllocationService;
import com.mediroute.mediroutebackend.common.models.PatientIncident;
import com.mediroute.mediroutebackend.common.models.ResourceType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Map;

// This controller exposes the allocation API so clients can request a resource assignment without interacting with the allocator logic directly.
// It validates incoming values and returns clear HTTP errors when the request is invalid or incomplete.
@RestController
@RequestMapping("/api/allocation")
public class AllocationController {

    private final AllocationService allocationService;

    public AllocationController(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    /**
     * Provides a lightweight health check that also verifies access to pending
     * incident data.
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        int pendingCount = allocationService.getPendingIncidents().size();
        String message = "Allocation module is alive. "
                + pendingCount
                + " pending incident(s) found.";

        return ResponseEntity.ok(message);
    }

    /** Returns all incidents currently waiting for resource allocation. */
    @GetMapping("/pending-requests")
    public ResponseEntity<List<PatientIncident>> getPendingRequests() {
        return ResponseEntity.ok(allocationService.getPendingIncidents());
    }

    /**
     * Allocates the available resources of the requested type using the algorithm
     * selected by the client.
     */
    @PostMapping("/assign")
    public ResponseEntity<AllocationResult> assign(
            @RequestBody AllocationRequestDTO request) {
        validateRequest(request, true);
        ResourceType resourceType = parseResourceType(request.getResourceType());
        AllocationResult result = allocationService.runAllocation(
                resourceType,
                request.getAlgorithm());

        return ResponseEntity.ok(result);
    }

    /**
     * Runs Greedy and Knapsack against the same pending incidents and resource
     * capacity so their results can be compared fairly.
     */
    @PostMapping("/compare")
    public ResponseEntity<Map<String, AllocationResult>> compare(
            @RequestBody AllocationRequestDTO request) {
        validateRequest(request, false);
        ResourceType resourceType = parseResourceType(request.getResourceType());

        return ResponseEntity.ok(allocationService.compareAlgorithms(resourceType));
    }

    /**
     * Normalizes user input before mapping it to the domain enum, allowing values
     * such as "ambulance" and "AMBULANCE" to be treated consistently.
     */
    private ResourceType parseResourceType(String resourceType) {
        try {
            return ResourceType.valueOf(resourceType.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported resource type: " + resourceType);
        }
    }

    /**
     * Validates fields shared by allocation requests. The comparison endpoint
     * does not require an algorithm because it always executes both algorithms.
     */
    private void validateRequest(
            AllocationRequestDTO request,
            boolean algorithmRequired) {
        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Request body is required");
        }

        if (request.getResourceType() == null
                || request.getResourceType().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Resource type is required");
        }

        if (algorithmRequired
                && (request.getAlgorithm() == null
                || request.getAlgorithm().isBlank())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Algorithm is required");
        }
    }
}
