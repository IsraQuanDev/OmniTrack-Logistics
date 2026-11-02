package com.codigofacilito.alert.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad de dominio Alert (Alerta o incidente generado en la flota).
 */
@Table("alerts")
public class Alert {

    @Id
    private Long id;

    @Column("vehicle_id")
    private Long vehicleId;

    @Column("alert_type")
    private String alertType; // SPEED_LIMIT_EXCEEDED, GEOFENCE_VIOLATION, LOW_BATTERY, PANIC_BUTTON, ETA_DELAY

    @Column("severity")
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL

    @Column("message")
    private String message;

    @Column("latitude")
    private Double latitude;

    @Column("longitude")
    private Double longitude;

    @Column("current_value")
    private Double currentValue;

    @Column("threshold_value")
    private Double thresholdValue;

    @Column("resolved")
    private Boolean resolved = false;

    @Column("resolved_at")
    private LocalDateTime resolvedAt;

    @Column("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Alert() {
    }

    public Alert(Long id, Long vehicleId, String alertType, String severity, String message, Double latitude, Double longitude, Double currentValue, Double thresholdValue, Boolean resolved, LocalDateTime resolvedAt, LocalDateTime createdAt) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.alertType = alertType;
        this.severity = severity;
        this.message = message;
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentValue = currentValue;
        this.thresholdValue = thresholdValue;
        this.resolved = resolved != null ? resolved : false;
        this.resolvedAt = resolvedAt;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
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
