package com.mediroute.mediroutebackend.common.models.repository;

import com.mediroute.mediroutebackend.common.models.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    @Query("SELECT h FROM Hospital h JOIN FETCH h.node")
    List<Hospital> findAllWithNode();
}
