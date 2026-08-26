package com.mediroute.mediroutebackend.common.models;

import jakarta.persistence.*;

/** One supply item included in a saved dispatch plan.*/
@Entity
@Table(name = "dispatch_plan_item")
public class DispatchPlanItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dispatch_plan_id", nullable = false)
    private Long dispatchPlanId;

    @Column(name = "supply_item_id", nullable = false)
    private Long supplyItemId;

    public DispatchPlanItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDispatchPlanId() { return dispatchPlanId; }
    public void setDispatchPlanId(Long dispatchPlanId) { this.dispatchPlanId = dispatchPlanId; }
    public Long getSupplyItemId() { return supplyItemId; }
    public void setSupplyItemId(Long supplyItemId) { this.supplyItemId = supplyItemId; }
}
