package com.mediroute.mediroutebackend.incident.service;

import com.mediroute.mediroutebackend.common.models.Resource;
import com.mediroute.mediroutebackend.common.models.ResourceStatus;
import com.mediroute.mediroutebackend.common.models.SupplyItem;
import com.mediroute.mediroutebackend.common.models.SupplyItemStatus;
import com.mediroute.mediroutebackend.common.models.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Answers "how do I reset pending incidents?" - clears everything the demo
 * created and restores the system to a clean starting state:
 *   - all reported incidents deleted (queue back to empty)
 *   - all resources (ambulances, beds, etc.) back to AVAILABLE
 *   - all supply items back to PENDING
 *   - all saved dispatch plans cleared
 *
 * Deliberately does NOT touch network_node, road_edge, or hospital rows -
 * those represent the physical city/hospital layout, not day-to-day
 * operational state, so they should never need resetting.
 */
@Service
public class ResetService {

    @Autowired private PatientIncidentRepository patientIncidentRepository;
    @Autowired private ResourceRepository resourceRepository;
    @Autowired private SupplyItemRepository supplyItemRepository;
    @Autowired private DispatchPlanRepository dispatchPlanRepository;
    @Autowired private DispatchPlanItemRepository dispatchPlanItemRepository;

    @Transactional
    public void resetDemoData() {
        // Clear dispatch history first - dispatch_plan_item references
        // dispatch_plan, so it must go first to respect the foreign key.
        dispatchPlanItemRepository.deleteAll();
        dispatchPlanRepository.deleteAll();

        // Clear every reported incident - queue goes back to empty.
        patientIncidentRepository.deleteAll();

        // Every resource (ambulances, ICU beds, ventilators, ward beds)
        // back to AVAILABLE.
        List<Resource> resources = resourceRepository.findAll();
        resources.forEach(r -> r.setStatus(ResourceStatus.AVAILABLE));
        resourceRepository.saveAll(resources);

        // Every supply item back to PENDING.
        List<SupplyItem> items = supplyItemRepository.findAll();
        items.forEach(i -> i.setStatus(SupplyItemStatus.PENDING));
        supplyItemRepository.saveAll(items);
    }
}
