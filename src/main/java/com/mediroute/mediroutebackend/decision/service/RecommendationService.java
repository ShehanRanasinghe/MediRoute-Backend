package com.mediroute.mediroutebackend.decision.service;

import com.mediroute.mediroutebackend.common.models.Hospital;
import com.mediroute.mediroutebackend.common.models.repository.HospitalRepository;
import com.mediroute.mediroutebackend.decision.algorithm.HospitalRecommender;
import com.mediroute.mediroutebackend.decision.algorithm.ScoringEngine;
import com.mediroute.mediroutebackend.decision.model.HospitalRecommendation;
import com.mediroute.mediroutebackend.decision.model.RecommendationRequest;
import com.mediroute.mediroutebackend.decision.model.RecommendationResult;
import com.mediroute.mediroutebackend.decision.util.GeoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads all hospitals from Supabase, scores each one against the incoming
 * patient request, and selects the top-k using either algorithm.
 */
@Service
public class RecommendationService {

    @Autowired
    private HospitalRepository hospitalRepository;

    private final ScoringEngine scoringEngine = new ScoringEngine();
    private final HospitalRecommender hospitalRecommender = new HospitalRecommender();

    public RecommendationResult recommend(RecommendationRequest request, String algorithm) {
        List<HospitalRecommendation> candidates = buildCandidates(request);
        int k = request.getTopK() != null ? request.getTopK() : 3;

        if (algorithm != null && algorithm.equalsIgnoreCase("fullsort")) {
            return hospitalRecommender.selectTopKUsingFullSort(candidates, k);
        }
        return hospitalRecommender.selectTopKUsingHeap(candidates, k);
    }

    public Map<String, RecommendationResult> compareAlgorithms(RecommendationRequest request) {
        List<HospitalRecommendation> candidates = buildCandidates(request);
        int k = request.getTopK() != null ? request.getTopK() : 3;

        Map<String, RecommendationResult> results = new HashMap<>();
        results.put("heap", hospitalRecommender.selectTopKUsingHeap(candidates, k));
        results.put("fullSort", hospitalRecommender.selectTopKUsingFullSort(candidates, k));
        return results;
    }

    private List<HospitalRecommendation> buildCandidates(RecommendationRequest request) {
        List<Hospital> hospitals = hospitalRepository.findAllWithNode();

        if (hospitals.isEmpty()) {
            // Fallback so the module still works before Supabase is fully seeded
            hospitals = buildFallbackHospitals();
        }

        List<HospitalRecommendation> candidates = new ArrayList<>();
        for (Hospital hospital : hospitals) {
            boolean specialtyMatch = hospital.getSpecialty() != null && request.getConditionType() != null
                    && hospital.getSpecialty().toUpperCase().contains(request.getConditionType().toUpperCase());

            double distanceKm = GeoUtils.haversineDistanceKm(
                    request.getPatientLatitude(), request.getPatientLongitude(),
                    hospital.getNode().getLatitude(), hospital.getNode().getLongitude());

            double availabilityRatio = hospital.getTotalBeds() == 0 ? 0
                    : (double) hospital.getAvailableBeds() / hospital.getTotalBeds();

            double score = scoringEngine.computeScore(specialtyMatch, distanceKm, availabilityRatio);

            HospitalRecommendation candidate = new HospitalRecommendation();
            candidate.setHospitalId(hospital.getId());
            candidate.setHospitalName(hospital.getNode().getName());
            candidate.setScore(score);
            candidate.setDistanceKm(distanceKm);
            candidate.setSpecialtyMatch(specialtyMatch);
            candidate.setAvailableBeds(hospital.getAvailableBeds());
            candidates.add(candidate);
        }
        return candidates;
    }

    /** Matches Task 1's seed data (hospitals at nodes 1 and 5) so results stay consistent. */
    private List<Hospital> buildFallbackHospitals() {
        // NOTE: without a real NetworkNode relationship, this fallback returns
        // an empty list rather than constructing detached entities - see
        // README "Known limitation" for why, and run database/schema.sql +
        // seed-data-task4.sql to avoid needing this fallback at all.
        return new ArrayList<>();
    }
}
