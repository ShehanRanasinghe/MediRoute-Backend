package com.mediroute.mediroutebackend.decision.algorithm;

/**
 * Computes a single weighted "suitability score" for a hospital, combining
 * three factors: specialty match, distance, and bed availability.
 *
 * WHY WEIGHTED SCORING (explainability):
 * Each factor's contribution is a clear, tunable number - if asked "why
 * was this hospital recommended?", the answer is always traceable back to
 * these three weighted terms, unlike a black-box model. This matters for
 * a healthcare decision-support tool where trust in the recommendation
 * reasoning is important.
 *
 * score = W_SPECIALTY x specialtyScore
 *       + W_DISTANCE   x distanceScore
 *       + W_AVAILABILITY x availabilityScore
 *
 * - specialtyScore: 1.0 if the hospital treats the patient's condition, else 0.0
 * - distanceScore: 1 / (1 + distanceKm) - closer hospitals score higher,
 *   bounded to (0, 1] so no single very-close hospital dominates unfairly
 * - availabilityScore: availableBeds / totalBeds, already in [0, 1]
 *
 */
public class ScoringEngine {

    private static final double WEIGHT_SPECIALTY = 0.5;
    private static final double WEIGHT_DISTANCE = 0.3;
    private static final double WEIGHT_AVAILABILITY = 0.2;

    public double computeScore(boolean specialtyMatch, double distanceKm, double bedAvailabilityRatio) {
        double specialtyScore = specialtyMatch ? 1.0 : 0.0;
        double distanceScore = 1.0 / (1.0 + distanceKm);
        double availabilityScore = bedAvailabilityRatio;

        return WEIGHT_SPECIALTY * specialtyScore
                + WEIGHT_DISTANCE * distanceScore
                + WEIGHT_AVAILABILITY * availabilityScore;
    }
}
