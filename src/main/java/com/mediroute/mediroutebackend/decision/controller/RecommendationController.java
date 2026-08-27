package com.mediroute.mediroutebackend.decision.controller;

import com.mediroute.mediroutebackend.decision.model.RecommendationRequest;
import com.mediroute.mediroutebackend.decision.model.RecommendationResult;
import com.mediroute.mediroutebackend.decision.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// This controller exposes the hospital recommendation API for the frontend and external clients.
// It takes a patient requirement, calls the decision service, and returns ranked hospital options in JSON format.
@RestController
@RequestMapping("/api/decision")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/ping")
    public String ping() {
        return "Decision module is alive.";
    }

    @PostMapping("/recommend")
    public ResponseEntity<RecommendationResult> recommend(
            @RequestBody RecommendationRequest request,
            @RequestParam(required = false) String algorithm) {
        return ResponseEntity.ok(recommendationService.recommend(request, algorithm));
    }

    @PostMapping("/compare")
    public ResponseEntity<Map<String, RecommendationResult>> compare(@RequestBody RecommendationRequest request) {
        return ResponseEntity.ok(recommendationService.compareAlgorithms(request));
    }
}
