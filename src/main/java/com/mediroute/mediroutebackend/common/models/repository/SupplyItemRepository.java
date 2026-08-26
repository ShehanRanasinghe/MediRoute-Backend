package com.mediroute.mediroutebackend.common.models.repository;

import com.mediroute.mediroutebackend.common.models.SupplyItem;
import com.mediroute.mediroutebackend.common.models.SupplyItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplyItemRepository extends JpaRepository<SupplyItem, Long> {
    List<SupplyItem> findByStatus(SupplyItemStatus status);
}
