package com.mediroute.mediroutebackend.incident.controller;

import com.mediroute.mediroutebackend.incident.model.DashboardSummary;
import com.mediroute.mediroutebackend.incident.model.IncidentReportRequest;
import com.mediroute.mediroutebackend.incident.model.IncidentResponse;
import com.mediroute.mediroutebackend.incident.model.IncidentSummaryView;
import com.mediroute.mediroutebackend.incident.service.IncidentOrchestrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// This controller is the main dispatcher API and coordinates the full incident workflow across the system.
// It sends the report to the orchestration layer so the decision, allocation, routing, and optimization services can work together.
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

    // Admin panel only - see IncidentSummaryView for the security note on
    // this endpoint (not protected at the API level, gated only by the
    // frontend admin login).
    @GetMapping("/list")
    public ResponseEntity<List<IncidentSummaryView>> getAllIncidents() {
        return ResponseEntity.ok(orchestrationService.getAllIncidents());
    }
}
