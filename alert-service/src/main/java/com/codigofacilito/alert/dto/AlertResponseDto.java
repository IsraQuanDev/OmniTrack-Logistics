package com.codigofacilito.alert.dto;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para Alertas, con enlaces HATEOAS y trazabilidad.
 */
public class AlertResponseDto extends RepresentationModel<AlertResponseDto> {

    private Long id;
    private Long vehicleId;
    private String alertType;
    private String severity;
    private String message;
    private Double latitude;
    private Double longitude;
    private Double currentValue;
    private Double thresholdValue;
    private Boolean resolved;
    private LocalDateTime resolvedAt;
    private LocalDateTime createdAt;

    public AlertResponseDto() {
    }

    public AlertResponseDto(Long id, Long vehicleId, String alertType, String severity, String message, Double latitude, Double longitude, Double currentValue, Double thresholdValue, Boolean resolved, LocalDateTime resolvedAt, LocalDateTime createdAt) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.alertType = alertType;
        this.severity = severity;
        this.message = message;
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentValue = currentValue;
        this.thresholdValue = thresholdValue;
        this.resolved = resolved;
        this.resolvedAt = resolvedAt;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
    }

    public Double getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(Double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
