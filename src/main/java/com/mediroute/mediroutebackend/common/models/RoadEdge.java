package com.mediroute.mediroutebackend.common.models;

import jakarta.persistence.*;

/**
 * A road connecting two NetworkNodes.
 *
 * Owner: Shehan
 */
@Entity
@Table(name = "road_edge")
public class RoadEdge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_node_id", nullable = false)
    private NetworkNode fromNode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_node_id", nullable = false)
    private NetworkNode toNode;

    @Column(name = "distance_km", nullable = false)
    private double distanceKm;

    @Column(name = "travel_time_minutes", nullable = false)
    private double travelTimeMinutes;

    @Column(nullable = false)
    private boolean bidirectional = true;

    public RoadEdge() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public NetworkNode getFromNode() { return fromNode; }
    public void setFromNode(NetworkNode fromNode) { this.fromNode = fromNode; }
    public NetworkNode getToNode() { return toNode; }
    public void setToNode(NetworkNode toNode) { this.toNode = toNode; }
    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }
    public double getTravelTimeMinutes() { return travelTimeMinutes; }
    public void setTravelTimeMinutes(double travelTimeMinutes) { this.travelTimeMinutes = travelTimeMinutes; }
    public boolean isBidirectional() { return bidirectional; }
    public void setBidirectional(boolean bidirectional) { this.bidirectional = bidirectional; }
}
