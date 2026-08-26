package com.mediroute.mediroutebackend.common.models.repository;

import com.mediroute.mediroutebackend.common.models.IncidentStatus;
import com.mediroute.mediroutebackend.common.models.PatientIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * What: Provides persistence operations and status-based lookups for patient incidents.
 * Why: Keeps incident data access in one place so services do not depend on database details.
 * How: Extends Spring Data JPA's standard CRUD repository and uses derived query methods.
 */
@Repository
public interface PatientIncidentRepository extends JpaRepository<PatientIncident, Long> {
    /**
     * What: Retrieves every patient incident with the requested status.
     * Why: Allows workflows to process incidents by lifecycle stage, such as pending or resolved.
     * How: Spring Data JPA derives the query from the {@code findByStatus} method name.
     *
     * @param status the incident status used to filter the results
     * @return matching incidents, or an empty list when no incidents have the given status
     */
    List<PatientIncident> findByStatus(IncidentStatus status);
}
