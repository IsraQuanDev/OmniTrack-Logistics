package com.codigofacilito.alert.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO para la creación manual o disparo de alertas.
 */
public class AlertRequestDto {

    @NotNull(message = "El identificador del vehículo es obligatorio")
    private Long vehicleId;

    @NotBlank(message = "El tipo de alerta es obligatorio")
    @Pattern(regexp = "^(SPEED_LIMIT_EXCEEDED|GEOFENCE_VIOLATION|LOW_BATTERY|PANIC_BUTTON|ETA_DELAY|LOW_FUEL|UNAUTHORIZED_STOP)$",
            message = "El tipo de alerta debe ser: SPEED_LIMIT_EXCEEDED, GEOFENCE_VIOLATION, LOW_BATTERY, PANIC_BUTTON, ETA_DELAY, LOW_FUEL o UNAUTHORIZED_STOP")
    private String alertType;

    @NotBlank(message = "La severidad es obligatoria")
    @Pattern(regexp = "^(LOW|MEDIUM|HIGH|CRITICAL)$", message = "La severidad debe ser LOW, MEDIUM, HIGH o CRITICAL")
    private String severity;

    @NotBlank(message = "El mensaje descriptivo de la alerta es obligatorio")
    private String message;

    private Double latitude;
    private Double longitude;
    private Double currentValue;
    private Double thresholdValue;

    public AlertRequestDto() {
    }

    public AlertRequestDto(Long vehicleId, String alertType, String severity, String message, Double latitude, Double longitude, Double currentValue, Double thresholdValue) {
        this.vehicleId = vehicleId;
        this.alertType = alertType;
        this.severity = severity;
        this.message = message;
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentValue = currentValue;
        this.thresholdValue = thresholdValue;
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
}
