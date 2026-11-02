package com.codigofacilito.route.service;

/**
 * Utilidades matemáticas para cálculos geoespaciales y algoritmos geodésicos (Fórmula de Haversine).
 */
public final class GeoSpatialUtils {

    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double EARTH_RADIUS_METERS = 6371000.0;

    private GeoSpatialUtils() {
    }

    /**
     * Calcula la distancia ortodrómica en kilómetros entre dos coordenadas geográficas.
     *
     * @param lat1 Latitud punto 1 en grados decimales
     * @param lon1 Longitud punto 1 en grados decimales
     * @param lat2 Latitud punto 2 en grados decimales
     * @param lon2 Longitud punto 2 en grados decimales
     * @return Distancia en kilómetros
     */
    public static double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    /**
     * Calcula la distancia en metros entre dos coordenadas geográficas.
     */
    public static double calculateDistanceMeters(double lat1, double lon1, double lat2, double lon2) {
        return calculateDistanceKm(lat1, lon1, lat2, lon2) * 1000.0;
    }

    /**
     * Determina si un punto coordenado se encuentra dentro del radio de una geocerca circular.
     */
    public static boolean isPointInsideGeofence(double centerLat, double centerLon, double radiusMeters, double pointLat, double pointLon) {
        double distanceMeters = calculateDistanceMeters(centerLat, centerLon, pointLat, pointLon);
        return distanceMeters <= radiusMeters;
    }
}
