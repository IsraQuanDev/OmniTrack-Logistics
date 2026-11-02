package com.codigofacilito.fleet.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad de dominio Shipment (Envío / Despacho de carga).
 */
@Table("shipments")
public class Shipment {

    @Id
    private Long id;

    @Column("tracking_number")
    private String trackingNumber;

    @Column("vehicle_id")
    private Long vehicleId;

    @Column("origin_address")
    private String originAddress;

    @Column("origin_latitude")
    private Double originLatitude;

    @Column("origin_longitude")
    private Double originLongitude;

    @Column("dest_address")
    private String destAddress;

    @Column("dest_latitude")
    private Double destLatitude;

    @Column("dest_longitude")
    private Double destLongitude;

    @Column("status")
    private String status; // CREATED, IN_TRANSIT, DELIVERED, CANCELLED

    @Column("estimated_delivery_time")
    private LocalDateTime estimatedDeliveryTime;

    @Column("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Shipment() {
    }

    public Shipment(Long id, String trackingNumber, Long vehicleId, String originAddress, Double originLatitude, Double originLongitude, String destAddress, Double destLatitude, Double destLongitude, String status, LocalDateTime estimatedDeliveryTime, LocalDateTime createdAt) {
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
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
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
