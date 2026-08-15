// WHAT: Task 4 recommendation engine — filters unsafe hospitals, scores the rest with a weighted
//       multi-criteria formula, and ranks them in a binary search tree.

// WHY: An emergency dispatcher must pick the most suitable hospital for one patient using
//      condition, location, severity, specialty match, distance, and bed / ICU availability.
//      The nearest hospital is not always clinically correct, so distance-only methods (k-NN)
//      are rejected. This class is the selected algorithm from the Task 4 report.

// HOW: 1) Decision-tree hard filters drop wrong-specialty or zero-capacity hospitals.
//      2) Each survivor is scored: 0.45*specialty + 0.35*distance + 0.20*availability.
//      3) Scored hospitals are inserted into SuitabilityBst; reverse in-order yields the ranking.
//      Distance uses Haversine until Task 1 routing can supply road travel time.

package com.mediroute.mediroutebackend.recommendation;

import com.mediroute.mediroutebackend.recommendation.model.HospitalProfile;
import com.mediroute.mediroutebackend.recommendation.model.RankedHospital;
import com.mediroute.mediroutebackend.recommendation.model.RecommendationRequest;
import com.mediroute.mediroutebackend.recommendation.model.RecommendationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HospitalRecommender {

    public static final double WEIGHT_SPECIALTY = 0.45;
    public static final double WEIGHT_DISTANCE = 0.35;
    public static final double WEIGHT_AVAILABILITY = 0.20;
    public static final int CRITICAL_SEVERITY_THRESHOLD = 8;

    private static final double EXACT_SPECIALTY_SCORE = 1.0;
    private static final double GENERAL_SPECIALTY_SCORE = 0.6;

    public RecommendationResult recommend(RecommendationRequest request, List<HospitalProfile> hospitals) {
        long startTime = System.nanoTime();

        RecommendationResult result = new RecommendationResult();
        result.setAlgorithmUsed("Weighted multi-criteria scoring + BST ranking");

        if (hospitals == null || hospitals.isEmpty()) {
            result.setRecommendationFound(false);
            result.setExecutionTimeNanos(System.nanoTime() - startTime);
            return result;
        }

        String condition = normalize(request.getConditionType());
        int severity = request.getSeverityScore();
        boolean icuRequired = severity >= CRITICAL_SEVERITY_THRESHOLD;

        List<RankedHospital> rejected = new ArrayList<>();
        SuitabilityBst rankingTree = new SuitabilityBst();

        for (HospitalProfile hospital : hospitals) {
            double distanceKm = HaversineCalculator.distanceKm(
                    request.getLatitude(), request.getLongitude(),
                    hospital.getLatitude(), hospital.getLongitude());

            String rejection = hardConstraintViolation(hospital, condition, icuRequired);
            if (rejection != null) {
                RankedHospital rejectedHospital = baseRankedHospital(hospital, distanceKm);
                rejectedHospital.setRejectionReason(rejection);
                rejected.add(rejectedHospital);
                continue;
            }

            RankedHospital scored = scoreHospital(hospital, condition, distanceKm, icuRequired);
            rankingTree.insert(scored);
        }

        result.setRejectedHospitals(rejected);
        result.setExecutionTimeNanos(System.nanoTime() - startTime);

        if (rankingTree.isEmpty()) {
            result.setRecommendationFound(false);
            result.setRankedHospitals(new ArrayList<>());
            return result;
        }

        List<RankedHospital> ranked = rankingTree.toRankedList();
        RankedHospital best = rankingTree.findBest();
        result.setRecommendationFound(true);
        result.setRankedHospitals(ranked);
        result.setRecommendedHospitalId(best.getHospitalId());
        result.setRecommendedHospitalName(best.getHospitalName());
        result.setRecommendedScore(best.getTotalScore());
        return result;
    }

    private String hardConstraintViolation(HospitalProfile hospital, String condition, boolean icuRequired) {
        if (specialtyScore(hospital.getSpecialty(), condition) <= 0.0) {
            return "Specialty does not match condition " + condition;
        }
        if (icuRequired && hospital.getAvailableIcuBeds() <= 0) {
            return "Critical severity requires an ICU bed, none available";
        }
        if (!icuRequired && hospital.getAvailableBeds() <= 0) {
            return "No ward beds available";
        }
        return null;
    }

    private RankedHospital scoreHospital(HospitalProfile hospital, String condition,
                                         double distanceKm, boolean icuRequired) {
        double specialty = specialtyScore(hospital.getSpecialty(), condition);
        double distance = 1.0 / (1.0 + distanceKm);
        double availability = availabilityScore(hospital, icuRequired);
        double total = WEIGHT_SPECIALTY * specialty
                + WEIGHT_DISTANCE * distance
                + WEIGHT_AVAILABILITY * availability;

        RankedHospital ranked = baseRankedHospital(hospital, distanceKm);
        ranked.setSpecialtyScore(round4(specialty));
        ranked.setDistanceScore(round4(distance));
        ranked.setAvailabilityScore(round4(availability));
        ranked.setTotalScore(round4(total));
        return ranked;
    }

    double specialtyScore(String specialtyCsv, String condition) {
        List<String> tags = parseSpecialties(specialtyCsv);
        if (tags.contains(condition)) {
            return EXACT_SPECIALTY_SCORE;
        }
        if (tags.contains("GENERAL")) {
            return GENERAL_SPECIALTY_SCORE;
        }
        return 0.0;
    }

    private double availabilityScore(HospitalProfile hospital, boolean icuRequired) {
        if (icuRequired) {
            if (hospital.getTotalIcuBeds() <= 0) {
                return 0.0;
            }
            return clamp01((double) hospital.getAvailableIcuBeds() / hospital.getTotalIcuBeds());
        }
        if (hospital.getTotalBeds() <= 0) {
            return 0.0;
        }
        return clamp01((double) hospital.getAvailableBeds() / hospital.getTotalBeds());
    }

    private RankedHospital baseRankedHospital(HospitalProfile hospital, double distanceKm) {
        RankedHospital ranked = new RankedHospital();
        ranked.setHospitalId(hospital.getId());
        ranked.setHospitalName(hospital.getName());
        ranked.setSpecialty(hospital.getSpecialty());
        ranked.setDistanceKm(round4(distanceKm));
        return ranked;
    }

    private List<String> parseSpecialties(String specialtyCsv) {
        List<String> tags = new ArrayList<>();
        if (specialtyCsv == null || specialtyCsv.isBlank()) {
            return tags;
        }
        for (String part : specialtyCsv.split(",")) {
            String tag = normalize(part);
            if (!tag.isEmpty()) {
                tags.add(tag);
            }
        }
        return tags;
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toUpperCase(Locale.ROOT);
    }

    private double clamp01(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    private double round4(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
}
