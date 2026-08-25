package com.mediroute.mediroutebackend.decision.util;

/**
 * Haversine formula - great-circle (straight-line) distance in km between
 * two lat/long points. Same formula Task 1's A* uses as its heuristic;
 * shared here as a small standalone utility since Task 4 needs it too but
 * doesn't otherwise depend on Task 1's routing code.
 *
 */
public class GeoUtils {

    private static final double EARTH_RADIUS_KM = 6371.0;

    public static double haversineDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}
