package com.mediroute.mediroutebackend.allocation.service;

import com.mediroute.allocation.algorithm.GreedyAllocator;
import com.mediroute.allocation.algorithm.KnapsackAllocator;
import com.mediroute.allocation.model.AllocationRequest;
import com.mediroute.allocation.model.AllocationResult;
import com.mediroute.common.models.IncidentStatus;
import com.mediroute.common.models.PatientIncident;
import com.mediroute.common.models.ResourceStatus;
import com.mediroute.common.models.ResourceType;
import com.mediroute.common.models.repository.PatientIncidentRepository;
import com.mediroute.common.models.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class AllocationService {

    @Autowired
    private PatientIncidentRepository incidentRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    private final GreedyAllocator greedyAllocator = new GreedyAllocator();
    private final KnapsackAllocator knapsackAllocator = new KnapsackAllocator();

    public AllocationResult runAllocation(ResourceType resourceType, String algorithm) {
        List<AllocationRequest> requests = loadPendingRequests();
        int capacity = countAvailableResources(resourceType);

        if (algorithm != null && algorithm.equalsIgnoreCase("knapsack")) {
            return knapsackAllocator.allocate(requests, capacity);
        }
        return greedyAllocator.allocate(requests, capacity);
    }

    public Map<String, AllocationResult> compareAlgorithms(ResourceType resourceType) {
        List<AllocationRequest> requests = loadPendingRequests();
        int capacity = countAvailableResources(resourceType);

        Map<String, AllocationResult> results = new HashMap<>();
        results.put("greedy", greedyAllocator.allocate(requests, capacity));
        results.put("knapsack", knapsackAllocator.allocate(requests, capacity));
        return results;
    }

    public List<PatientIncident> getPendingIncidents() {
        return incidentRepository.findByStatus(IncidentStatus.PENDING);
    }

    private List<AllocationRequest> loadPendingRequests() {
        return incidentRepository.findByStatus(IncidentStatus.PENDING).stream()
                .map(incident -> new AllocationRequest(
                        incident.getId(),
                        incident.getConditionType(),
                        incident.getSeverityScore(),
                        // ASSUMPTION: critical incidents (severity >= 8) need 2 resource
                        // units; standard incidents need 1. See docs/01-problem-analysis.md
                        incident.getSeverityScore() >= 8 ? 2 : 1
                ))
                .collect(Collectors.toList());
    }

    private int countAvailableResources(ResourceType resourceType) {
        return resourceRepository.findByResourceTypeAndStatus(resourceType, ResourceStatus.AVAILABLE).size();
    }
}
