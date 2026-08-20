package com.mediroute.mediroutebackend.common.models.repository;

import com.mediroute.mediroutebackend.common.models.Resource;
import com.mediroute.mediroutebackend.common.models.ResourceStatus;
import com.mediroute.mediroutebackend.common.models.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * What: Provides persistence operations and filtered lookups for medical resources.
 * Why: Centralizes resource data access so allocation logic remains separate from persistence.
 * How: Extends Spring Data JPA's standard CRUD repository and uses derived query methods.
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    /**
     * What: Retrieves resources that match both a resource type and an operational status.
     * Why: Enables callers to find suitable resources for allocation without filtering in memory.
     * How: Spring Data JPA derives the query from the
     * {@code findByResourceTypeAndStatus} method name.
     *
     * @param resourceType the category of resource to retrieve
     * @param status the operational status used to filter resources
     * @return matching resources, or an empty list when no resources satisfy both criteria
     */
    List<Resource> findByResourceTypeAndStatus(ResourceType resourceType, ResourceStatus status);
}
