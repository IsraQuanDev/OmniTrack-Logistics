package com.codigofacilito.route.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad de dominio para el registro histórico de cálculos de ruta y ETA.
 */
@Table("route_calculations")
public class RouteCalculationRecord {

    @Id
    private Long id;

    @Column("vehicle_id")
    private Long vehicleId;

    @Column("origin_latitude")
    private Double originLatitude;

    @Column("origin_longitude")
    private Double originLongitude;

    @Column("dest_latitude")
    private Double destLatitude;

    @Column("dest_longitude")
    private Double destLongitude;

    @Column("current_latitude")
    private Double currentLatitude;

    @Column("current_longitude")
    private Double currentLongitude;

    @Column("current_speed_kmh")
    private Double currentSpeedKmh;

    @Column("distance_remaining_km")
    private Double distanceRemainingKm;

    @Column("eta_minutes")
    private Double etaMinutes;

    @Column("estimated_arrival_timestamp")
    private LocalDateTime estimatedArrivalTimestamp;

    @Column("calculated_at")
    private LocalDateTime calculatedAt = LocalDateTime.now();

    public RouteCalculationRecord() {
    }

    public RouteCalculationRecord(Long id, Long vehicleId, Double originLatitude, Double originLongitude, Double destLatitude, Double destLongitude, Double currentLatitude, Double currentLongitude, Double currentSpeedKmh, Double distanceRemainingKm, Double etaMinutes, LocalDateTime estimatedArrivalTimestamp, LocalDateTime calculatedAt) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.originLatitude = originLatitude;
        this.originLongitude = originLongitude;
        this.destLatitude = destLatitude;
        this.destLongitude = destLongitude;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.currentSpeedKmh = currentSpeedKmh;
        this.distanceRemainingKm = distanceRemainingKm;
        this.etaMinutes = etaMinutes;
        this.estimatedArrivalTimestamp = estimatedArrivalTimestamp;
        this.calculatedAt = calculatedAt != null ? calculatedAt : LocalDateTime.now();
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

    public Double getOriginLatitude() {
        return originLatitude;
    }

    public void setOriginLatitude(Double originLatitude) {
        this.originLatitude = originLatitude;
    }

    public Double getOriginLongitude() {
        return originLongitude;
    }

    public void setOriginLongitude(Double originLongitude) {
        this.originLongitude = originLongitude;
    }

    public Double getDestLatitude() {
        return destLatitude;
    }

    public void setDestLatitude(Double destLatitude) {
        this.destLatitude = destLatitude;
    }

    public Double getDestLongitude() {
        return destLongitude;
    }

    public void setDestLongitude(Double destLongitude) {
        this.destLongitude = destLongitude;
    }

    public Double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(Double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public Double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(Double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public Double getCurrentSpeedKmh() {
        return currentSpeedKmh;
    }

    public void setCurrentSpeedKmh(Double currentSpeedKmh) {
        this.currentSpeedKmh = currentSpeedKmh;
    }

    public Double getDistanceRemainingKm() {
        return distanceRemainingKm;
    }

    public void setDistanceRemainingKm(Double distanceRemainingKm) {
        this.distanceRemainingKm = distanceRemainingKm;
    }

    public Double getEtaMinutes() {
        return etaMinutes;
    }

    public void setEtaMinutes(Double etaMinutes) {
        this.etaMinutes = etaMinutes;
    }

    public LocalDateTime getEstimatedArrivalTimestamp() {
        return estimatedArrivalTimestamp;
    }

    public void setEstimatedArrivalTimestamp(LocalDateTime estimatedArrivalTimestamp) {
        this.estimatedArrivalTimestamp = estimatedArrivalTimestamp;
    }

    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(LocalDateTime calculatedAt) {
        this.calculatedAt = calculatedAt;
    }
}
