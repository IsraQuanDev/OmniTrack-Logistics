package com.codigofacilito.route.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para la creación y edición de geocercas.
 */
public class GeofenceRequestDto {

    @NotBlank(message = "El nombre de la geocerca es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String name;

    private String description;

    @NotNull(message = "La latitud central es obligatoria")
    @DecimalMin(value = "-90.0", message = "La latitud central debe ser >= -90.0")
    @DecimalMax(value = "90.0", message = "La latitud central debe ser <= 90.0")
    private Double centerLatitude;

    @NotNull(message = "La longitud central es obligatoria")
    @DecimalMin(value = "-180.0", message = "La longitud central debe ser >= -180.0")
    @DecimalMax(value = "180.0", message = "La longitud central debe ser <= 180.0")
    private Double centerLongitude;

    @NotNull(message = "El radio en metros es obligatorio")
    @DecimalMin(value = "10.0", message = "El radio mínimo debe ser de al menos 10 metros")
    private Double radiusMeters;

    private Boolean active = true;

    private Long vehicleId;

    public GeofenceRequestDto() {
    }

    public GeofenceRequestDto(String name, String description, Double centerLatitude, Double centerLongitude, Double radiusMeters, Boolean active, Long vehicleId) {
        this.name = name;
        this.description = description;
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.radiusMeters = radiusMeters;
        this.active = active != null ? active : true;
        this.vehicleId = vehicleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCenterLatitude() {
        return centerLatitude;
    }

    public void setCenterLatitude(Double centerLatitude) {
        this.centerLatitude = centerLatitude;
    }

    public Double getCenterLongitude() {
        return centerLongitude;
    }

    public void setCenterLongitude(Double centerLongitude) {
        this.centerLongitude = centerLongitude;
    }

    public Double getRadiusMeters() {
        return radiusMeters;
    }

    public void setRadiusMeters(Double radiusMeters) {
        this.radiusMeters = radiusMeters;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }
}
