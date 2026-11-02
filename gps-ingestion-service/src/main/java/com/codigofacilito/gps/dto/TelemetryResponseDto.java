package com.codigofacilito.gps.dto;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para lecturas de telemetría GPS, con soporte HATEOAS.
 */
public class TelemetryResponseDto extends RepresentationModel<TelemetryResponseDto> {

    private Long id;
    private Long vehicleId;
    private String deviceId;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Double speedKmh;
    private Double heading;
    private Double batteryLevel;
    private Double fuelLevel;
    private LocalDateTime timestamp;

    public TelemetryResponseDto() {
    }

    public TelemetryResponseDto(Long id, Long vehicleId, String deviceId, Double latitude, Double longitude, Double altitude, Double speedKmh, Double heading, Double batteryLevel, Double fuelLevel, LocalDateTime timestamp) {
        this.id = id;
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
