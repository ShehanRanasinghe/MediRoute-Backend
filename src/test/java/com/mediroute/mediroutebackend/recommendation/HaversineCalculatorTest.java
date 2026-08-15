package com.mediroute.mediroutebackend.recommendation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HaversineCalculatorTest {

    @Test
    void samePointHasZeroDistance() {
        double distance = HaversineCalculator.distanceKm(6.9271, 79.8612, 6.9271, 79.8612);
        assertEquals(0.0, distance, 0.0001);
    }

    @Test
    void cityHospitalIsCloserToIncidentThanGeneralHospital() {
        double toCity = HaversineCalculator.distanceKm(6.9285, 79.8625, 6.9271, 79.8612);
        double toGeneral = HaversineCalculator.distanceKm(6.9285, 79.8625, 6.9400, 79.8750);

        assertTrue(toCity < toGeneral);
        assertTrue(toCity > 0.0);
    }
}
