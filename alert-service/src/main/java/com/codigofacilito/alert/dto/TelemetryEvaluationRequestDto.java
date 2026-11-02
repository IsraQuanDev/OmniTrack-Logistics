package com.codigofacilito.alert.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para someter un reporte de telemetría a evaluación automática de anomalías y umbrales.
 */
public class TelemetryEvaluationRequestDto {

    @NotNull(message = "El identificador del vehículo es obligatorio")
    private Long vehicleId;

    private Double speedKmh;
    private Double latitude;
    private Double longitude;
    private Double batteryLevel;
    private Double fuelLevel;
    private Boolean panicButtonPressed = false;

    public TelemetryEvaluationRequestDto() {
    }

    public TelemetryEvaluationRequestDto(Long vehicleId, Double speedKmh, Double latitude, Double longitude, Double batteryLevel, Double fuelLevel, Boolean panicButtonPressed) {
        this.vehicleId = vehicleId;
        this.speedKmh = speedKmh;
        this.latitude = latitude;
        this.longitude = longitude;
        this.batteryLevel = batteryLevel;
        this.fuelLevel = fuelLevel;
        this.panicButtonPressed = panicButtonPressed != null ? panicButtonPressed : false;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Double getSpeedKmh() {
        return speedKmh;
    }

    public void setSpeedKmh(Double speedKmh) {
        this.speedKmh = speedKmh;
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

    public Double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Double getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(Double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public Boolean getPanicButtonPressed() {
        return panicButtonPressed;
    }

    public void setPanicButtonPressed(Boolean panicButtonPressed) {
        this.panicButtonPressed = panicButtonPressed;
    }
}
