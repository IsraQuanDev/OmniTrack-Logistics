package com.codigofacilito.route.dto;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para Geocercas con enlaces HATEOAS.
 */
public class GeofenceResponseDto extends RepresentationModel<GeofenceResponseDto> {

    private Long id;
    private String name;
    private String description;
    private Double centerLatitude;
    private Double centerLongitude;
    private Double radiusMeters;
    private Boolean active;
    private Long vehicleId;
    private LocalDateTime createdAt;

    public GeofenceResponseDto() {
    }

    public GeofenceResponseDto(Long id, String name, String description, Double centerLatitude, Double centerLongitude, Double radiusMeters, Boolean active, Long vehicleId, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.radiusMeters = radiusMeters;
        this.active = active;
        this.vehicleId = vehicleId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
