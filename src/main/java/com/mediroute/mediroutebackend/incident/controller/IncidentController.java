package com.mediroute.mediroutebackend.incident.controller;

import com.mediroute.mediroutebackend.incident.model.DashboardSummary;
import com.mediroute.mediroutebackend.incident.model.IncidentReportRequest;
import com.mediroute.mediroutebackend.incident.model.IncidentResponse;
import com.mediroute.mediroutebackend.incident.service.IncidentOrchestrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The single API surface the real-world frontend actually talks to for
 * the main dispatcher workflow. The 5 modules' own controllers
 * (RoutingController, AllocationController, etc.) still exist underneath
 * and still work - this controller just orchestrates them together.
 */
@RestController
@RequestMapping("/api/incident")
public class IncidentController {

    @Autowired
    private IncidentOrchestrationService orchestrationService;

    @PostMapping("/report")
    public ResponseEntity<IncidentResponse> reportIncident(@RequestBody IncidentReportRequest request) {
        return ResponseEntity.ok(orchestrationService.handleNewIncident(request));
    }

    @GetMapping("/dashboard-summary")
    public ResponseEntity<DashboardSummary> getDashboardSummary() {
        return ResponseEntity.ok(orchestrationService.getDashboardSummary());
    }
}
