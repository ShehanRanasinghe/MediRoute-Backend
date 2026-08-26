package com.mediroute.mediroutebackend.common.models;

import jakarta.persistence.*;

/** One supply item included in a saved dispatch plan.*/
@Entity
@Table(name = "dispatch_plan_item")
public class DispatchPlanItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatch_plan_id", nullable = false)
    private DispatchPlan dispatchPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supply_item_id", nullable = false)
    private SupplyItem supplyItem;

    public DispatchPlanItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public DispatchPlan getDispatchPlan() { return dispatchPlan; }
    public void setDispatchPlan(DispatchPlan dispatchPlan) { this.dispatchPlan = dispatchPlan; }
    public SupplyItem getSupplyItem() { return supplyItem; }
    public void setSupplyItem(SupplyItem supplyItem) { this.supplyItem = supplyItem; }
}
