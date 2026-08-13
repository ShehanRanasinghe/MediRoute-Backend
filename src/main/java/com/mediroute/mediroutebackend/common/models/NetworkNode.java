package com.mediroute.mediroutebackend.common.models;

import jakarta.persistence.*;

/**
 * A point in the shared hospital/road network graph.
 * Used directly by Task 1 (Route Optimization) and Task 3 (Network Analysis).
 *
 * Owner: Shehan
 */
@Entity
@Table(name = "network_node")
public class NetworkNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "node_type", nullable = false, length = 20)
    private NodeType nodeType;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    public NetworkNode() {
    }

    public NetworkNode(String name, NodeType nodeType, double latitude, double longitude) {
        this.name = name;
        this.nodeType = nodeType;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public NodeType getNodeType() { return nodeType; }
    public void setNodeType(NodeType nodeType) { this.nodeType = nodeType; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}
