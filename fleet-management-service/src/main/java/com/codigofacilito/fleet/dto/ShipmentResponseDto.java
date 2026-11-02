package com.codigofacilito.fleet.dto;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para Shipment, con soporte de hipervínculos HATEOAS.
 */
public class ShipmentResponseDto extends RepresentationModel<ShipmentResponseDto> {

    private Long id;
    private String trackingNumber;
    private Long vehicleId;
    private String originAddress;
    private Double originLatitude;
    private Double originLongitude;
    private String destAddress;
    private Double destLatitude;
    private Double destLongitude;
    private String status;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime createdAt;

    public ShipmentResponseDto() {
    }

    public ShipmentResponseDto(Long id, String trackingNumber, Long vehicleId, String originAddress, Double originLatitude, Double originLongitude, String destAddress, Double destLatitude, Double destLongitude, String status, LocalDateTime estimatedDeliveryTime, LocalDateTime createdAt) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.vehicleId = vehicleId;
        this.originAddress = originAddress;
        this.originLatitude = originLatitude;
        this.originLongitude = originLongitude;
        this.destAddress = destAddress;
        this.destLatitude = destLatitude;
        this.destLongitude = destLongitude;
        this.status = status;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(String originAddress) {
        this.originAddress = originAddress;
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

    public String getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
