package com.mediroute.mediroutebackend.common.models.repository;

import com.mediroute.mediroutebackend.common.models.Resource;
import com.mediroute.mediroutebackend.common.models.ResourceStatus;
import com.mediroute.mediroutebackend.common.models.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// This repository handles the database access for medical resource records.
// It keeps the persistence logic separate from the allocation logic, which makes the code easier to maintain.
// The service layer calls this repository whenever it needs to inspect the live inventory.
@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    // This query returns only resources that match the selected type and current status.
    // The allocation service uses this to find usable ambulance, bed, or ventilator records quickly.
    // @param resourceType the resource category to search for
    // @param status the resource availability state to filter by
    // @return the matching resource list for that pool
    List<Resource> findByResourceTypeAndStatus(ResourceType resourceType, ResourceStatus status);
}
