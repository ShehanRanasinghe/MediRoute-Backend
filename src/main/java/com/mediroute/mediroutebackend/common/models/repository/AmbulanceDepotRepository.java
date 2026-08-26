package com.mediroute.mediroutebackend.common.models.repository;

import com.mediroute.mediroutebackend.common.models.AmbulanceDepot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Missing piece from earlier tasks - none of Task 1-5 created a repository
 * for AmbulanceDepot even though the entity existed. Needed now because
 * the integration layer looks up a depot's location to start routing from.
 *
 * Uses the same JOIN FETCH pattern as HospitalRepository (Task 4) to avoid
 * LazyInitializationException when reading depot.getNode().

 */
@Repository
public interface AmbulanceDepotRepository extends JpaRepository<AmbulanceDepot, Long> {

    @Query("SELECT d FROM AmbulanceDepot d JOIN FETCH d.node")
    List<AmbulanceDepot> findAllWithNode();
}
