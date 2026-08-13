package com.mediroute.mediroutebackend.common.models.repository;

import com.mediroute.common.models.RoadEdge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Owner: Shehan

@Repository
public interface RoadEdgeRepository extends JpaRepository<RoadEdge, Long> {
}
