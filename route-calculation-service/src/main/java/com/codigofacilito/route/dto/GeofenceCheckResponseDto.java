package com.codigofacilito.route.dto;

/**
 * DTO con el resultado de la verificación perimetral de una geocerca.
 */
public class GeofenceCheckResponseDto {

    private Long geofenceId;
    private String geofenceName;
    private Boolean isInside;
    private String status; // INSIDE, OUTSIDE
    private Double distanceToCenterMeters;
    private Double radiusMeters;

    public GeofenceCheckResponseDto() {
    }

    public GeofenceCheckResponseDto(Long geofenceId, String geofenceName, Boolean isInside, String status, Double distanceToCenterMeters, Double radiusMeters) {
        this.geofenceId = geofenceId;
        this.geofenceName = geofenceName;
        this.isInside = isInside;
        this.status = status;
        this.distanceToCenterMeters = distanceToCenterMeters;
        this.radiusMeters = radiusMeters;
    }

    public Long getGeofenceId() {
        return geofenceId;
    }

    public void setGeofenceId(Long geofenceId) {
        this.geofenceId = geofenceId;
    }

    public String getGeofenceName() {
        return geofenceName;
    }

    public void setGeofenceName(String geofenceName) {
        this.geofenceName = geofenceName;
    }

    public Boolean getIsInside() {
        return isInside;
    }

    public void setIsInside(Boolean inside) {
        isInside = inside;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getDistanceToCenterMeters() {
        return distanceToCenterMeters;
    }

    public void setDistanceToCenterMeters(Double distanceToCenterMeters) {
        this.distanceToCenterMeters = distanceToCenterMeters;
    }

    public Double getRadiusMeters() {
        return radiusMeters;
    }

    public void setRadiusMeters(Double radiusMeters) {
        this.radiusMeters = radiusMeters;
    }
}
