package com.codigofacilito.route.dto;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para cálculo de ETA y distancia remanente con soporte HATEOAS.
 */
public class EtaCalculationResponseDto extends RepresentationModel<EtaCalculationResponseDto> {

    private Long id;
    private Long vehicleId;
    private Double currentLatitude;
    private Double currentLongitude;
    private Double destLatitude;
    private Double destLongitude;
    private Double currentSpeedKmh;
    private Double distanceRemainingKm;
    private Double etaMinutes;
    private String etaFormatted;
    private LocalDateTime estimatedArrivalTimestamp;
    private LocalDateTime calculatedAt;

    public EtaCalculationResponseDto() {
    }

    public EtaCalculationResponseDto(Long id, Long vehicleId, Double currentLatitude, Double currentLongitude, Double destLatitude, Double destLongitude, Double currentSpeedKmh, Double distanceRemainingKm, Double etaMinutes, String etaFormatted, LocalDateTime estimatedArrivalTimestamp, LocalDateTime calculatedAt) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.destLatitude = destLatitude;
        this.destLongitude = destLongitude;
        this.currentSpeedKmh = currentSpeedKmh;
        this.distanceRemainingKm = distanceRemainingKm;
        this.etaMinutes = etaMinutes;
        this.etaFormatted = etaFormatted;
        this.estimatedArrivalTimestamp = estimatedArrivalTimestamp;
        this.calculatedAt = calculatedAt;
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

    public String getEtaFormatted() {
        return etaFormatted;
    }

    public void setEtaFormatted(String etaFormatted) {
        this.etaFormatted = etaFormatted;
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
