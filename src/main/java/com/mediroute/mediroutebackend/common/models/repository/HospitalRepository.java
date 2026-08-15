// WHAT: Spring Data JPA repository for reading and writing Hospital records.

// WHY: HospitalRecommendationService loads every hospital (specialty, beds, ICU, linked GPS node)
//      before scoring. This interface provides that access without hand-written SQL.

// HOW: Extending JpaRepository gives findAll(), findById(), save(), and delete() at runtime.
//      The node association is EAGER on the entity, so findAll() also brings name and coordinates.

package com.mediroute.mediroutebackend.common.models.repository;

import com.mediroute.mediroutebackend.common.models.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
