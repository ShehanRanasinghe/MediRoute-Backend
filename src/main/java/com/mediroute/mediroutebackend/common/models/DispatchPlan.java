package com.mediroute.mediroutebackend.common.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * A permanent record of one dispatch decision: which incident it was for,
 * which algorithm chose the loadout, and the resulting value/capacity
 * numbers. Previously this information existed only in memory for the
 * duration of one HTTP request and was never saved.
 */
@Entity
@Table(name = "dispatch_plan")
public class DispatchPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id")
    private PatientIncident incident;

    @Column(name = "algorithm_used", length = 50)
    private String algorithmUsed;

    @Column(name = "total_value_achieved", nullable = false)
    private int totalValueAchieved;

    @Column(name = "capacity_used", nullable = false)
    private int capacityUsed;

    @Column(name = "total_capacity", nullable = false)
    private int totalCapacity;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public DispatchPlan() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PatientIncident getIncident() { return incident; }
    public void setIncident(PatientIncident incident) { this.incident = incident; }
    public String getAlgorithmUsed() { return algorithmUsed; }
    public void setAlgorithmUsed(String algorithmUsed) { this.algorithmUsed = algorithmUsed; }
    public int getTotalValueAchieved() { return totalValueAchieved; }
    public void setTotalValueAchieved(int totalValueAchieved) { this.totalValueAchieved = totalValueAchieved; }
    public int getCapacityUsed() { return capacityUsed; }
    public void setCapacityUsed(int capacityUsed) { this.capacityUsed = capacityUsed; }
    public int getTotalCapacity() { return totalCapacity; }
    public void setTotalCapacity(int totalCapacity) { this.totalCapacity = totalCapacity; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
