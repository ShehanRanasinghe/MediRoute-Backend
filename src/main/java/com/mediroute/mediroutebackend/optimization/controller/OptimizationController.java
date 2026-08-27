package com.mediroute.mediroutebackend.optimization.controller;

import com.mediroute.mediroutebackend.optimization.model.OptimizationRequestDTO;
import com.mediroute.mediroutebackend.optimization.model.OptimizationResult;
import com.mediroute.mediroutebackend.optimization.service.OptimizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


// This controller exposes the dispatch optimization API for choosing a supply loadout under a given vehicle capacity.
// It routes requests to the service layer and passes the selected algorithm choice through to the optimizer.
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
