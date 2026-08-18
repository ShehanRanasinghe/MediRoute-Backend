package com.mediroute.common.models.repository;

import com.mediroute.common.models.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * WHY A CUSTOM "JOIN FETCH" QUERY:
 * Hospital.node is a LAZY relationship (see Hospital.java) - calling
 * hospital.getNode() after the repository call returns can throw
 * LazyInitializationException once Hibernate's session has closed,
 * depending on transaction boundaries. Using "JOIN FETCH" here explicitly
 * loads the NetworkNode data in the SAME query, avoiding the problem
 * entirely instead of relying on Spring Boot's default open-session-in-view
 * behavior (which works, but is considered a code smell to depend on).
 *
 * Owner: Chamika
 */
@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    @Query("SELECT h FROM Hospital h JOIN FETCH h.node")
    List<Hospital> findAllWithNode();
}
