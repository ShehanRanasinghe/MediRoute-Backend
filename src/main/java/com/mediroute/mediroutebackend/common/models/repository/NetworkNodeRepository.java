package com.mediroute.mediroutebackend.common.models.repository;

import com.mediroute.common.models.NetworkNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Owner: Shehan

@Repository
public interface NetworkNodeRepository extends JpaRepository<NetworkNode, Long> {
}
