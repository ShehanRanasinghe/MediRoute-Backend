package com.mediroute.mediroutebackend.common.models;

import jakarta.persistence.*;

/**
 * What: Represents an ambulance depot and its ambulance capacity.
 * Why: The dispatch system needs to know which depots can provide an ambulance.
 * How: It links a depot to a network node and stores total and available counts.
 */
@Entity
@Table(name = "ambulance_depot")
public class AmbulanceDepot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The unique routing-network location represented by this depot. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id", nullable = false, unique = true)
    private NetworkNode node;

    @Column(name = "total_ambulances", nullable = false)
    private int totalAmbulances;

    @Column(name = "available_ambulances", nullable = false)
    private int availableAmbulances;

    public AmbulanceDepot() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public NetworkNode getNode() { return node; }
    public void setNode(NetworkNode node) { this.node = node; }
    public int getTotalAmbulances() { return totalAmbulances; }
    public void setTotalAmbulances(int totalAmbulances) { this.totalAmbulances = totalAmbulances; }
    public int getAvailableAmbulances() { return availableAmbulances; }
    public void setAvailableAmbulances(int availableAmbulances) { this.availableAmbulances = availableAmbulances; }
}
