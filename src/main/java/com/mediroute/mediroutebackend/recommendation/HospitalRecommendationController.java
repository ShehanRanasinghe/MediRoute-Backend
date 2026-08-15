// WHAT: REST endpoint that accepts a patient incident and returns a ranked hospital recommendation.

// WHY: Task 4 must expose Best Hospital Recommendation to a dispatcher / frontend. This controller
//      is the HTTP boundary; scoring stays inside HospitalRecommender.

// HOW: POST /api/recommendations with a JSON RecommendationRequest. Bean Validation rejects
//      missing coordinates, blank condition, or severity outside 1–10. The service loads hospitals
//      from the database and returns a RecommendationResult.

package com.mediroute.mediroutebackend.recommendation;

import com.mediroute.mediroutebackend.recommendation.model.RecommendationRequest;
import com.mediroute.mediroutebackend.recommendation.model.RecommendationResult;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
public class HospitalRecommendationController {

    private final HospitalRecommendationService hospitalRecommendationService;

    public HospitalRecommendationController(HospitalRecommendationService hospitalRecommendationService) {
        this.hospitalRecommendationService = hospitalRecommendationService;
    }

    @PostMapping
    public ResponseEntity<RecommendationResult> recommend(@Valid @RequestBody RecommendationRequest request) {
        RecommendationResult result = hospitalRecommendationService.recommend(request);
        return ResponseEntity.ok(result);
    }
}
