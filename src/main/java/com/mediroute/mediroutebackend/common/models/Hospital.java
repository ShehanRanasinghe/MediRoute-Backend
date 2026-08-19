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

    public Hospital() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public NetworkNode getNode() { return node; }
    public void setNode(NetworkNode node) { this.node = node; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public int getTotalBeds() { return totalBeds; }
    public void setTotalBeds(int totalBeds) { this.totalBeds = totalBeds; }
    public int getAvailableBeds() { return availableBeds; }
    public void setAvailableBeds(int availableBeds) { this.availableBeds = availableBeds; }
    public int getTotalIcuBeds() { return totalIcuBeds; }
    public void setTotalIcuBeds(int totalIcuBeds) { this.totalIcuBeds = totalIcuBeds; }
    public int getAvailableIcuBeds() { return availableIcuBeds; }
    public void setAvailableIcuBeds(int availableIcuBeds) { this.availableIcuBeds = availableIcuBeds; }
}
