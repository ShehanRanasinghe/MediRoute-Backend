// WHAT: Utility that computes the great-circle distance in kilometres between two GPS points.

// WHY: Task 1 routing is not required to finish before Task 4. Until Dijkstra / A* travel time
//      can be injected, the recommender uses this Haversine distance as the distance criterion.

// HOW: Standard Haversine formula with Earth radius 6371 km — the same constant AStarRouter uses
//      for its heuristic, so later swapping path cost for Haversine stays in the same unit.

package com.mediroute.mediroutebackend.recommendation;

public class HaversineCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0;

    public static double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
