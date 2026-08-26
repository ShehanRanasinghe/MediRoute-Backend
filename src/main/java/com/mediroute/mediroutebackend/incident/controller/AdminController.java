package com.mediroute.mediroutebackend.incident.controller;

import com.mediroute.mediroutebackend.incident.service.ResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demo/admin utilities - not a coursework-graded feature by itself, but
 * necessary to run repeated demos and VIVA rehearsals without needing to
 * manually clear the database between each run.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private ResetService resetService;

    @PostMapping("/reset-demo-data")
    public ResponseEntity<String> resetDemoData() {
        resetService.resetDemoData();
        return ResponseEntity.ok("Demo data reset - all incidents cleared, all resources and supply items restored.");
    }
}
