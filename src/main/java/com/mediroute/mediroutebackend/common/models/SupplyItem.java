package com.mediroute.mediroutebackend.common.models;

import jakarta.persistence.*;

/**
 * A medical supply crate or scheduled patient transfer task waiting to be
 * loaded onto an ambulance/vehicle at a depot - the "item" in the
 * knapsack sense for Task 5's optimization problem.
 *
 * NOTE: this is the first shared entity across all 5 modules that needs a
 * genuinely NEW database table - Tasks 2, 3, and 4 all reused tables
 * Task 1 already created. See database/schema-addition-task5.sql.
 */
@Entity
@Table(name = "supply_item")
public class SupplyItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name", nullable = false, length = 150)
    private String itemName;

    @Column(name = "item_type", length = 30)
    private String itemType;

    @Column(name = "urgency_value", nullable = false)
    private int urgencyValue;

    @Column(name = "size_cost", nullable = false)
    private int sizeCost;

    @Column(name = "depot_id")
    private Long depotId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SupplyItemStatus status = SupplyItemStatus.PENDING;

    public SupplyItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }
    public int getUrgencyValue() { return urgencyValue; }
    public void setUrgencyValue(int urgencyValue) { this.urgencyValue = urgencyValue; }
    public int getSizeCost() { return sizeCost; }
    public void setSizeCost(int sizeCost) { this.sizeCost = sizeCost; }
    public Long getDepotId() { return depotId; }
    public void setDepotId(Long depotId) { this.depotId = depotId; }
    public SupplyItemStatus getStatus() { return status; }
    public void setStatus(SupplyItemStatus status) { this.status = status; }
}