package com.mediroute.mediroutebackend.routing.controller;

import com.mediroute.mediroutebackend.routing.model.OptimizationRequestDTO;
import com.mediroute.mediroutebackend.routing.model.OptimizationResult;
import com.mediroute.mediroutebackend.routing.service.OptimizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST endpoints for the Optimization Module.
 *
 * Owner: Nethru
 */
@RestController
@RequestMapping("/api/optimization")
public class OptimizationController {

    @Autowired
    private OptimizationService optimizationService;

    @GetMapping("/ping")
    public String ping() {
        return "Optimization module is alive. " + optimizationService.getPendingItems().size()
                + " pending item(s) found.";
    }

    @PostMapping("/optimize")
    public ResponseEntity<OptimizationResult> optimize(@RequestBody OptimizationRequestDTO request) {
        return ResponseEntity.ok(optimizationService.optimize(request.getVehicleCapacity(), request.getAlgorithm()));
    }

    @PostMapping("/compare")
    public ResponseEntity<Map<String, OptimizationResult>> compare(@RequestBody OptimizationRequestDTO request) {
        return ResponseEntity.ok(optimizationService.compareAll(request.getVehicleCapacity()));
    }
}