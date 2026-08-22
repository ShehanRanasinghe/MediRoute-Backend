package com.mediroute.network.controller;

import com.mediroute.network.model.CentralityResult;
import com.mediroute.network.model.CriticalNodeResult;
import com.mediroute.network.model.MSTResult;
import com.mediroute.network.service.NetworkAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for the Network Analysis module.
 *

 */
@RestController
@RequestMapping("/api/network")
public class NetworkAnalysisController {

    @Autowired
    private NetworkAnalysisService networkAnalysisService;

    @GetMapping("/ping")
    public String ping() {
        return "Network analysis module is alive.";
    }

    @GetMapping("/critical-nodes")
    public ResponseEntity<CriticalNodeResult> getCriticalNodes() {
        return ResponseEntity.ok(networkAnalysisService.findCriticalNodes());
    }

    @GetMapping("/mst")
    public ResponseEntity<MSTResult> getMST() {
        return ResponseEntity.ok(networkAnalysisService.buildBackboneNetwork());
    }

    @GetMapping("/centrality-ranking")
    public ResponseEntity<CentralityResult> getCentralityRanking() {
        return ResponseEntity.ok(networkAnalysisService.rankNodesByCentrality());
    }
}
