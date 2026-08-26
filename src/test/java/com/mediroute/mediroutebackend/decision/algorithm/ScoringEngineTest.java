package com.mediroute.mediroutebackend.decision.algorithm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ScoringEngineTest {

    private final ScoringEngine engine = new ScoringEngine();

    @Test
    void specialtyMatchIncreasesScore() {
        double withMatch = engine.computeScore(true, 5.0, 0.5);
        double withoutMatch = engine.computeScore(false, 5.0, 0.5);
        assertTrue(withMatch > withoutMatch);
    }

    @Test
    void closerDistanceIncreasesScore() {
        double closer = engine.computeScore(true, 1.0, 0.5);
        double farther = engine.computeScore(true, 20.0, 0.5);
        assertTrue(closer > farther);
    }

    @Test
    void higherBedAvailabilityIncreasesScore() {
        double moreBeds = engine.computeScore(true, 5.0, 0.9);
        double fewerBeds = engine.computeScore(true, 5.0, 0.1);
        assertTrue(moreBeds > fewerBeds);
    }
}
