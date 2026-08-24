package com.mediroute.mediroutebackend.common.models;

import jakarta.persistence.*;

/**
 * What: Represents a hospital and its available treatment capacity.
 * Why: The dispatch system needs a suitable hospital for each patient.
 * How: It links the hospital to a network node and tracks its specialty and beds.
 */
@Entity
@Table(name = "hospital")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The unique routing-network location represented by this hospital. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id", nullable = false, unique = true)
    private NetworkNode node;

    @Column(length = 255)
    private String specialty;

    @Column(name = "total_beds", nullable = false)
    private int totalBeds;

    @Column(name = "available_beds", nullable = false)
    private int availableBeds;

    @Column(name = "total_icu_beds", nullable = false)
    private int totalIcuBeds;

    @Column(name = "available_icu_beds", nullable = false)
    private int availableIcuBeds;

    /** Creates an empty hospital entity for JPA and data binding. */
    public Hospital() {
    }

    /** Returns the database identifier of this hospital. */
    public Long getId() {
        return id;
    }

    /** Updates the database identifier of this hospital. */
    public void setId(Long id) {
        this.id = id;
    }

    /** Returns the routing-network node where this hospital is located. */
    public NetworkNode getNode() {
        return node;
    }

    /** Assigns the routing-network node where this hospital is located. */
    public void setNode(NetworkNode node) {
        this.node = node;
    }

    /** Returns the medical specialty offered by this hospital. */
    public String getSpecialty() {
        return specialty;
    }

    /** Updates the medical specialty offered by this hospital. */
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    /** Returns the hospital's total number of standard beds. */
    public int getTotalBeds() {
        return totalBeds;
    }

    /** Updates the hospital's total number of standard beds. */
    public void setTotalBeds(int totalBeds) {
        this.totalBeds = totalBeds;
    }

    /** Returns the number of standard beds currently available. */
    public int getAvailableBeds() {
        return availableBeds;
    }

    /** Updates the number of standard beds currently available. */
    public void setAvailableBeds(int availableBeds) {
        this.availableBeds = availableBeds;
    }

    /** Returns the hospital's total number of intensive-care beds. */
    public int getTotalIcuBeds() {
        return totalIcuBeds;
    }

    /** Updates the hospital's total number of intensive-care beds. */
    public void setTotalIcuBeds(int totalIcuBeds) {
        this.totalIcuBeds = totalIcuBeds;
    }

    /** Returns the number of intensive-care beds currently available. */
    public int getAvailableIcuBeds() {
        return availableIcuBeds;
    }

    /** Updates the number of intensive-care beds currently available. */
    public void setAvailableIcuBeds(int availableIcuBeds) {
        this.availableIcuBeds = availableIcuBeds;
    }
}
