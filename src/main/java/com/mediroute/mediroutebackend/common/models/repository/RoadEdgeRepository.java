// WHAT  : Spring Data JPA repository interface for reading and writing RoadEdge records from the database.

// WHY   : GraphLoaderService must load every road segment from Supabase at startup to build the weighted
//         adjacency list that Dijkstra and A* traverse; this interface provides that database access
//         without requiring hand-written SQL queries.

// HOW   : By extending JpaRepository<RoadEdge, Long>, Spring automatically generates a full CRUD +
//         pagination implementation at runtime. The @Repository annotation registers it as a Spring bean
//         so it can be injected into GraphLoaderService (or any service) via constructor injection.


package com.mediroute.mediroutebackend.common.models.repository; // Declares the package this interface belongs to

import com.mediroute.mediroutebackend.common.models.RoadEdge; // Imports the RoadEdge entity this repository manages
import org.springframework.data.jpa.repository.JpaRepository; // Imports the Spring Data base interface that provides built-in CRUD methods
import org.springframework.stereotype.Repository; // Imports the annotation that registers this interface as a Spring-managed bean

@Repository // Registers this interface as a Spring Data repository bean; also enables exception translation
public interface RoadEdgeRepository extends JpaRepository<RoadEdge, Long> { // Inherits findAll(), findById(), save(), delete(), and more for RoadEdge with Long primary key
}
