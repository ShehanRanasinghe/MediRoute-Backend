// WHAT: JUnit 5 tests for Task 4 HospitalRecommender using the seed Colombo hospitals.

// WHY: Coursework requires automated tests proving the selected algorithm (weighted scoring +
//      hard-constraint filter + BST ranking) chooses the clinically correct hospital.

// HOW: A two-hospital fixture matching seed-data.sql is built before each test. Cases cover
//      cardiac vs trauma specialty, critical ICU filtering, empty input, and BST order.

package com.mediroute.mediroutebackend.recommendation;

import com.mediroute.mediroutebackend.recommendation.model.HospitalProfile;
import com.mediroute.mediroutebackend.recommendation.model.RankedHospital;
import com.mediroute.mediroutebackend.recommendation.model.RecommendationRequest;
import com.mediroute.mediroutebackend.recommendation.model.RecommendationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HospitalRecommenderTest {

    private HospitalRecommender recommender;
    private HospitalProfile cityHospital;
    private HospitalProfile generalHospital;

    @BeforeEach
    void setUp() {
        recommender = new HospitalRecommender();
        cityHospital = new HospitalProfile(
                1L, "City Hospital", "CARDIAC,GENERAL",
                6.9271, 79.8612,
                120, 34, 12, 3);
        generalHospital = new HospitalProfile(
                2L, "General Hospital", "TRAUMA,GENERAL",
                6.9400, 79.8750,
                200, 58, 20, 7);
    }

    @Test
    void cardiacCriticalIncidentPrefersCityHospital() {
        RecommendationRequest request = new RecommendationRequest(6.9285, 79.8625, "CARDIAC", 9);

        RecommendationResult result = recommender.recommend(request, List.of(cityHospital, generalHospital));

        assertTrue(result.isRecommendationFound());
        assertEquals(1L, result.getRecommendedHospitalId());
        assertEquals("City Hospital", result.getRecommendedHospitalName());
        assertEquals("Weighted multi-criteria scoring + BST ranking", result.getAlgorithmUsed());
        assertFalse(result.getRankedHospitals().isEmpty());
        assertEquals(1L, result.getRankedHospitals().get(0).getHospitalId());
    }

    @Test
    void traumaIncidentPrefersGeneralHospital() {
        RecommendationRequest request = new RecommendationRequest(6.9420, 79.8770, "TRAUMA", 7);

        RecommendationResult result = recommender.recommend(request, List.of(cityHospital, generalHospital));

        assertTrue(result.isRecommendationFound());
        assertEquals(2L, result.getRecommendedHospitalId());
        assertEquals("General Hospital", result.getRecommendedHospitalName());
    }

    @Test
    void generalLowSeverityStillReturnsAHospital() {
        RecommendationRequest request = new RecommendationRequest(6.9235, 79.8580, "GENERAL", 3);

        RecommendationResult result = recommender.recommend(request, List.of(cityHospital, generalHospital));

        assertTrue(result.isRecommendationFound());
        assertNotNull(result.getRecommendedHospitalId());
        assertTrue(result.getRankedHospitals().size() >= 1);
    }

    @Test
    void criticalPatientRejectsHospitalWithNoIcuAndFallsBackToGeneral() {
        HospitalProfile fullIcuCardiac = new HospitalProfile(
                1L, "City Hospital", "CARDIAC,GENERAL",
                6.9271, 79.8612,
                120, 34, 12, 0);
        RecommendationRequest request = new RecommendationRequest(6.9285, 79.8625, "CARDIAC", 9);

        RecommendationResult result = recommender.recommend(request, List.of(fullIcuCardiac, generalHospital));

        assertTrue(result.isRecommendationFound());
        assertEquals(2L, result.getRecommendedHospitalId());
        assertEquals(1, result.getRejectedHospitals().size());
        assertTrue(result.getRejectedHospitals().get(0).getRejectionReason().contains("ICU"));
    }

    @Test
    void noFeasibleHospitalReturnsNotFound() {
        HospitalProfile fullIcuCardiac = new HospitalProfile(
                1L, "City Hospital", "CARDIAC",
                6.9271, 79.8612,
                120, 34, 12, 0);
        HospitalProfile traumaOnly = new HospitalProfile(
                2L, "General Hospital", "TRAUMA",
                6.9400, 79.8750,
                200, 58, 20, 7);
        RecommendationRequest request = new RecommendationRequest(6.9285, 79.8625, "CARDIAC", 9);

        RecommendationResult result = recommender.recommend(request, List.of(fullIcuCardiac, traumaOnly));

        assertFalse(result.isRecommendationFound());
        assertEquals(2, result.getRejectedHospitals().size());
    }

    @Test
    void unmatchedSpecialtyIsRejected() {
        HospitalProfile traumaOnly = new HospitalProfile(
                2L, "General Hospital", "TRAUMA",
                6.9400, 79.8750,
                200, 58, 20, 7);
        RecommendationRequest request = new RecommendationRequest(6.9285, 79.8625, "CARDIAC", 9);

        RecommendationResult result = recommender.recommend(request, List.of(traumaOnly));

        assertFalse(result.isRecommendationFound());
        assertEquals(1, result.getRejectedHospitals().size());
        assertTrue(result.getRejectedHospitals().get(0).getRejectionReason().contains("Specialty"));
    }

    @Test
    void emptyHospitalListReturnsNoRecommendation() {
        RecommendationRequest request = new RecommendationRequest(6.9285, 79.8625, "CARDIAC", 9);

        RecommendationResult result = recommender.recommend(request, List.of());

        assertFalse(result.isRecommendationFound());
        assertNull(result.getRecommendedHospitalId());
    }

    @Test
    void scoreBreakdownUsesConfiguredWeights() {
        RecommendationRequest request = new RecommendationRequest(6.9271, 79.8612, "CARDIAC", 9);

        RecommendationResult result = recommender.recommend(request, List.of(cityHospital));

        assertTrue(result.isRecommendationFound());
        RankedHospital ranked = result.getRankedHospitals().get(0);
        assertEquals(1.0, ranked.getSpecialtyScore(), 0.0001);
        assertEquals(1.0, ranked.getDistanceScore(), 0.0001);
        double expected = HospitalRecommender.WEIGHT_SPECIALTY * ranked.getSpecialtyScore()
                + HospitalRecommender.WEIGHT_DISTANCE * ranked.getDistanceScore()
                + HospitalRecommender.WEIGHT_AVAILABILITY * ranked.getAvailabilityScore();
        assertEquals(expected, ranked.getTotalScore(), 0.001);
    }
}
