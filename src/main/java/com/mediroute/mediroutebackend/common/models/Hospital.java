// WHAT: JPA entity that represents one hospital in the MediRoute network, including specialty
//       tags and live bed / ICU capacity used by Task 4 hospital recommendation.

// WHY: Task 4 must rank hospitals by specialty match, distance, and availability. Those fields
//      are not stored on NetworkNode (which is only a graph vertex). This 1-to-1 hospital row
//      holds the clinical data while reusing the shared node for name and GPS coordinates.

// HOW: Mapped to the "hospital" table. node_id is a unique foreign key to network_node so each
//      hospital occupies exactly one graph vertex. GraphLoader-style services fetch hospitals
//      through HospitalRepository; the recommendation module converts each row into an
//      in-memory HospitalProfile before scoring.

package com.mediroute.mediroutebackend.common.models;

import jakarta.persistence.*;

@Entity
@Table(name = "hospital")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "node_id", nullable = false, unique = true)
    private NetworkNode node;

    @Column(length = 255)
    private String specialty;

    @Column(name = "total_beds", nullable = false)
    private int totalBeds = 0;

    @Column(name = "available_beds", nullable = false)
    private int availableBeds = 0;

    @Column(name = "total_icu_beds", nullable = false)
    private int totalIcuBeds = 0;

    @Column(name = "available_icu_beds", nullable = false)
    private int availableIcuBeds = 0;

    public Hospital() {
    }

    public Hospital(NetworkNode node, String specialty, int totalBeds, int availableBeds,
                    int totalIcuBeds, int availableIcuBeds) {
        this.node = node;
        this.specialty = specialty;
        this.totalBeds = totalBeds;
        this.availableBeds = availableBeds;
        this.totalIcuBeds = totalIcuBeds;
        this.availableIcuBeds = availableIcuBeds;
    }

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
