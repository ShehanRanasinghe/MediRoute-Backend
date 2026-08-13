// WHAT  : Spring Data JPA repository interface for reading and writing NetworkNode records from the database.

// WHY   : GraphLoaderService needs to fetch all network nodes stored in Supabase at startup so the
//         in-memory graph can be built before any routing request arrives; this interface provides
//         that database access without requiring hand-written SQL.

// HOW   : By extending JpaRepository<NetworkNode, Long>, Spring automatically generates a full
//         CRUD + pagination implementation at runtime. The @Repository annotation registers it as
//         a Spring bean so it can be injected into any service via @Autowired or constructor injection.


package com.mediroute.mediroutebackend.common.models.repository; // Declares the package this interface belongs to

import com.mediroute.common.models.NetworkNode; // Imports the NetworkNode entity this repository manages
import org.springframework.data.jpa.repository.JpaRepository; // Imports the Spring Data base interface that provides built-in CRUD methods
import org.springframework.stereotype.Repository; // Imports the annotation that registers this interface as a Spring-managed bean

@Repository // Registers this interface as a Spring Data repository bean; also enables exception translation
public interface NetworkNodeRepository extends JpaRepository<NetworkNode, Long> { // Inherits findAll(), findById(), save(), delete(), and more for NetworkNode with Long primary key
}
