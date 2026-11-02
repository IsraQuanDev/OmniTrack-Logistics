package com.codigofacilito.route.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para verificar si una coordenada actual se encuentra dentro o fuera del perímetro de una geocerca.
 */
public class GeofenceCheckRequestDto {

    @NotNull(message = "El identificador de la geocerca es obligatorio")
    private Long geofenceId;

    @NotNull(message = "La latitud actual es obligatoria")
    @DecimalMin(value = "-90.0", message = "La latitud debe ser >= -90.0")
    @DecimalMax(value = "90.0", message = "La latitud debe ser <= 90.0")
    private Double latitude;

    @NotNull(message = "La longitud actual es obligatoria")
    @DecimalMin(value = "-180.0", message = "La longitud debe ser >= -180.0")
    @DecimalMax(value = "180.0", message = "La longitud debe ser <= 180.0")
    private Double longitude;

    public GeofenceCheckRequestDto() {
    }

    public GeofenceCheckRequestDto(Long geofenceId, Double latitude, Double longitude) {
        this.geofenceId = geofenceId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getGeofenceId() {
        return geofenceId;
    }

    public void setGeofenceId(Long geofenceId) {
        this.geofenceId = geofenceId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
