// WHAT: Spring MVC REST controller that exposes three HTTP endpoints for the Route Optimization module:
//       a health-check ping, a shortest-path query, and a side-by-side algorithm comparison.

// WHY: The Next.js frontend and any external client need a well-defined HTTP API to trigger routing
//      calculations without knowing anything about the internal algorithm or database structure.
//      This controller is the single entry point that receives HTTP requests and delegates all
//      computation to RoutingService, keeping the API layer thin and the business logic separate.

// HOW: Annotated with @RestController so Spring automatically serialises return values to JSON.
//      @RequestMapping("/api/routing") prefixes all endpoint paths with /api/routing.
//      Each method is mapped to a specific HTTP verb + path via @GetMapping / @PostMapping.
//      RoutingService is injected via @Autowired and handles all algorithm execution and graph access.

package com.mediroute.mediroutebackend.routing.controller; // Declares the package this class belongs to

import org.springframework.beans.factory.annotation.Autowired; // Imports @Autowired so Spring injects RoutingService automatically
import org.springframework.http.ResponseEntity; // Imports ResponseEntity to wrap responses with HTTP status codes
import org.springframework.web.bind.annotation.*; // Imports @RestController, @RequestMapping, @GetMapping, @PostMapping, @RequestBody

import com.mediroute.mediroutebackend.routing.model.RouteRequest; // Imports RouteRequest DTO used as @RequestBody in POST endpoints
import com.mediroute.mediroutebackend.routing.model.RouteResult;  // Imports RouteResult DTO returned from routing computations
import com.mediroute.mediroutebackend.routing.service.RoutingService; // Imports RoutingService bean to be injected via @Autowired

import java.util.Collection; // Imports Collection to return the nodes from the /nodes endpoint
import java.util.List; // Imports List to return an ordered collection of nodes from the /nodes endpoint
import java.util.Map; // Imports Map to hold the paired Dijkstra + A* results returned by the compare endpoint

@RestController // Marks this class as a REST controller; every method return value is written directly to the HTTP response body as JSON
@RequestMapping("/api/routing") // Sets the base URL path for all endpoints in this controller to /api/routing
public class RoutingController { // Defines the controller class that handles all incoming HTTP requests for the routing module

    @Autowired // Tells Spring to inject the RoutingService bean into this field at application startup
    private RoutingService routingService; // Service that owns the in-memory Graph and delegates work to DijkstraRouter / AStarRouter

    @GetMapping("/ping") // Maps HTTP GET /api/routing/ping to this method; used to verify the backend is running and the DB is loaded
    public String ping() { // Returns a plain-text status message confirming the module is alive and showing graph size
        return "Routing module is alive. Graph has " + routingService.getGraph().nodeCount()
                + " nodes and " + routingService.getGraph().edgeCount() + " edges."; // Reads node and edge counts from the in-memory Graph to confirm Supabase data was loaded
    }

    @GetMapping("/nodes") // Maps HTTP GET /api/routing/nodes; returns every network node so the frontend can populate route-form dropdowns from real DB data
    public ResponseEntity<Collection<com.mediroute.mediroutebackend.routing.model.Node>> getNodes() { // Returns all Node objects currently held in the in-memory graph (loaded from network_node table or fallback)
        return ResponseEntity.ok(routingService.getGraph().getAllNodes()); // Delegates to Graph.getAllNodes() which returns all registered Node instances
    }

    @PostMapping("/shortest-path") // Maps HTTP POST /api/routing/shortest-path to this method; the default algorithm (Dijkstra) is used
    public ResponseEntity<RouteResult> getShortestPath(@RequestBody RouteRequest request) { // Accepts a JSON body deserialised into RouteRequest (sourceId + destinationId) and returns the optimal route
        RouteResult result = routingService.computeRoute(request); // Delegates the route calculation to RoutingService which runs Dijkstra on the loaded Graph
        return ResponseEntity.ok(result); // Wraps the RouteResult in a 200 OK HTTP response and serialises it to JSON
    }

    @PostMapping("/compare") // Maps HTTP POST /api/routing/compare to this method; used by the frontend /routing page to show both algorithms
    public ResponseEntity<Map<String, RouteResult>> compareAlgorithms(@RequestBody RouteRequest request) { // Accepts the same RouteRequest and returns a Map with keys "dijkstra" and "aStar" for side-by-side comparison
        return ResponseEntity.ok(routingService.compareAlgorithms(request)); // Delegates to RoutingService which runs both Dijkstra and A* and returns their results in a single Map
    }
}
