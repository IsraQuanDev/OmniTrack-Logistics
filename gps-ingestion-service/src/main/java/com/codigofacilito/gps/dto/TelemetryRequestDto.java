package com.codigofacilito.gps.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO para la ingesta de telemetría GPS con validaciones de límites geográficos y sensores.
 */
public class TelemetryRequestDto {

    @NotNull(message = "El identificador del vehículo es obligatorio")
    private Long vehicleId;

    @NotBlank(message = "El identificador del dispositivo GPS es obligatorio")
    private String deviceId;

    @NotNull(message = "La latitud es obligatoria")
    @DecimalMin(value = "-90.0", message = "La latitud debe estar entre -90.0 y 90.0 grados")
    @DecimalMax(value = "90.0", message = "La latitud debe estar entre -90.0 y 90.0 grados")
    private Double latitude;

    @NotNull(message = "La longitud es obligatoria")
    @DecimalMin(value = "-180.0", message = "La longitud debe estar entre -180.0 y 180.0 grados")
    @DecimalMax(value = "180.0", message = "La longitud debe estar entre -180.0 y 180.0 grados")
    private Double longitude;

    private Double altitude;

    @NotNull(message = "La velocidad en km/h es obligatoria")
    @Min(value = 0, message = "La velocidad no puede ser negativa")
    private Double speedKmh;

    @DecimalMin(value = "0.0", message = "El rumbo debe estar entre 0.0 y 360.0 grados")
    @DecimalMax(value = "360.0", message = "El rumbo debe estar entre 0.0 y 360.0 grados")
    private Double heading;

    @DecimalMin(value = "0.0", message = "El nivel de batería debe ser >= 0%")
    @DecimalMax(value = "100.0", message = "El nivel de batería debe ser <= 100%")
    private Double batteryLevel;

    @DecimalMin(value = "0.0", message = "El nivel de combustible debe ser >= 0%")
    @DecimalMax(value = "100.0", message = "El nivel de combustible debe ser <= 100%")
    private Double fuelLevel;

    private LocalDateTime timestamp;

    public TelemetryRequestDto() {
    }

    public TelemetryRequestDto(Long vehicleId, String deviceId, Double latitude, Double longitude, Double altitude, Double speedKmh, Double heading, Double batteryLevel, Double fuelLevel, LocalDateTime timestamp) {
        this.vehicleId = vehicleId;
        this.deviceId = deviceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.speedKmh = speedKmh;
        this.heading = heading;
        this.batteryLevel = batteryLevel;
        this.fuelLevel = fuelLevel;
        this.timestamp = timestamp;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getSpeedKmh() {
        return speedKmh;
    }

    public void setSpeedKmh(Double speedKmh) {
        this.speedKmh = speedKmh;
    }

    public Double getHeading() {
        return heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
