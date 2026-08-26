package com.mediroute.mediroutebackend.common.models;

import jakarta.persistence.*;


/**
 * What: Represents a medical asset managed by the dispatch system.
 * Why: The system must know which resources exist and whether they can be used.
 * How: It stores the resource type, owner, and current availability status.
 */
@Entity
@Table(name = "resource")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false, length = 20)
    private ResourceType resourceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false, length = 20)
    private OwnerType ownerType;

    /**
     * Identifier of the owning hospital or depot; {@link #ownerType} determines
     * which entity type this value references.
     */
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ResourceStatus status = ResourceStatus.AVAILABLE;

    /** Creates an empty resource entity with an available default status. */
    public Resource() {
    }

    /** Returns the database identifier of this medical resource. */
    public Long getId() {
        return id;
    }

    /** Updates the database identifier of this medical resource. */
    public void setId(Long id) {
        this.id = id;
    }

    /** Returns the category that describes this medical resource. */
    public ResourceType getResourceType() {
        return resourceType;
    }

    /** Updates the category that describes this medical resource. */
    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    /** Returns the type of facility that owns this resource. */
    public OwnerType getOwnerType() {
        return ownerType;
    }

    /** Updates the type of facility that owns this resource. */
    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    /** Returns the identifier of the hospital or depot that owns this resource. */
    public Long getOwnerId() {
        return ownerId;
    }

    /** Updates the identifier of the hospital or depot that owns this resource. */
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    /** Returns the current operational status of this resource. */
    public ResourceStatus getStatus() {
        return status;
    }

    /** Updates the current operational status of this resource. */
    public void setStatus(ResourceStatus status) {
        this.status = status;
    }
}
