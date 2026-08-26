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

    /** Creates an empty depot entity for JPA and data binding. */
    public AmbulanceDepot() {
    }

    /** Returns the database identifier of this depot. */
    public Long getId() {
        return id;
    }

    /** Updates the database identifier of this depot. */
    public void setId(Long id) {
        this.id = id;
    }

    /** Returns the routing-network node where this depot is located. */
    public NetworkNode getNode() {
        return node;
    }

    /** Assigns the routing-network node where this depot is located. */
    public void setNode(NetworkNode node) {
        this.node = node;
    }

    /** Returns the total number of ambulances assigned to this depot. */
    public int getTotalAmbulances() {
        return totalAmbulances;
    }

    /** Updates the total number of ambulances assigned to this depot. */
    public void setTotalAmbulances(int totalAmbulances) {
        this.totalAmbulances = totalAmbulances;
    }

    /** Returns the number of ambulances currently available for dispatch. */
    public int getAvailableAmbulances() {
        return availableAmbulances;
    }

    /** Updates the number of ambulances currently available for dispatch. */
    public void setAvailableAmbulances(int availableAmbulances) {
        this.availableAmbulances = availableAmbulances;
    }
}
