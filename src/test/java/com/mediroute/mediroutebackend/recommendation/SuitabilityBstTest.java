package com.mediroute.mediroutebackend.recommendation;

import com.mediroute.mediroutebackend.recommendation.model.RankedHospital;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SuitabilityBstTest {

    @Test
    void reverseInOrderReturnsHighestScoreFirst() {
        SuitabilityBst tree = new SuitabilityBst();
        tree.insert(hospital(1L, "Low", 0.20));
        tree.insert(hospital(2L, "High", 0.90));
        tree.insert(hospital(3L, "Mid", 0.50));

        List<RankedHospital> ranked = tree.toRankedList();

        assertEquals(3, ranked.size());
        assertEquals(2L, ranked.get(0).getHospitalId());
        assertEquals(3L, ranked.get(1).getHospitalId());
        assertEquals(1L, ranked.get(2).getHospitalId());
        assertEquals(2L, tree.findBest().getHospitalId());
    }

    @Test
    void equalScoresAreOrderedByHospitalId() {
        SuitabilityBst tree = new SuitabilityBst();
        tree.insert(hospital(5L, "B", 0.70));
        tree.insert(hospital(2L, "A", 0.70));

        List<RankedHospital> ranked = tree.toRankedList();

        assertEquals(2L, ranked.get(0).getHospitalId());
        assertEquals(5L, ranked.get(1).getHospitalId());
    }

    @Test
    void emptyTreeHasNoBestHospital() {
        SuitabilityBst tree = new SuitabilityBst();

        assertTrue(tree.isEmpty());
        assertNull(tree.findBest());
        assertTrue(tree.toRankedList().isEmpty());
    }

    private RankedHospital hospital(Long id, String name, double score) {
        RankedHospital ranked = new RankedHospital();
        ranked.setHospitalId(id);
        ranked.setHospitalName(name);
        ranked.setTotalScore(score);
        return ranked;
    }
}
