package com.codigofacilito.gps.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad de dominio para el registro de telemetría y coordenadas GPS recibidas de los dispositivos.
 */
@Table("telemetry_records")
public class TelemetryRecord {

    @Id
    private Long id;

    @Column("vehicle_id")
    private Long vehicleId;

    @Column("device_id")
    private String deviceId;

    @Column("latitude")
    private Double latitude;

    @Column("longitude")
    private Double longitude;

    @Column("altitude")
    private Double altitude;

    @Column("speed_kmh")
    private Double speedKmh;

    @Column("heading")
    private Double heading;

    @Column("battery_level")
    private Double batteryLevel;

    @Column("fuel_level")
    private Double fuelLevel;

    @Column("timestamp")
    private LocalDateTime timestamp = LocalDateTime.now();

    public TelemetryRecord() {
    }

    public TelemetryRecord(Long id, Long vehicleId, String deviceId, Double latitude, Double longitude, Double altitude, Double speedKmh, Double heading, Double batteryLevel, Double fuelLevel, LocalDateTime timestamp) {
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
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
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
