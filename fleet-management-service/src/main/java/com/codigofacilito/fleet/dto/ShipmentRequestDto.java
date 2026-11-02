package com.codigofacilito.fleet.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO para la creación y despacho de envíos (Shipments).
 */
public class ShipmentRequestDto {

    @NotBlank(message = "El número de guía o tracking es obligatorio")
    @Size(min = 5, max = 50, message = "El número de guía debe tener entre 5 y 50 caracteres")
    private String trackingNumber;

    @NotNull(message = "El identificador del vehículo asignado es obligatorio")
    private Long vehicleId;

    @NotBlank(message = "La dirección de origen es obligatoria")
    private String originAddress;

    @NotNull(message = "La latitud de origen es obligatoria")
    @DecimalMin(value = "-90.0", message = "La latitud de origen debe ser >= -90")
    @DecimalMax(value = "90.0", message = "La latitud de origen debe ser <= 90")
    private Double originLatitude;

    @NotNull(message = "La longitud de origen es obligatoria")
    @DecimalMin(value = "-180.0", message = "La longitud de origen debe ser >= -180")
    @DecimalMax(value = "180.0", message = "La longitud de origen debe ser <= 180")
    private Double originLongitude;

    @NotBlank(message = "La dirección de destino es obligatoria")
    private String destAddress;

    @NotNull(message = "La latitud de destino es obligatoria")
    @DecimalMin(value = "-90.0", message = "La latitud de destino debe ser >= -90")
    @DecimalMax(value = "90.0", message = "La latitud de destino debe ser <= 90")
    private Double destLatitude;

    @NotNull(message = "La longitud de destino es obligatoria")
    @DecimalMin(value = "-180.0", message = "La longitud de destino debe ser >= -180")
    @DecimalMax(value = "180.0", message = "La longitud de destino debe ser <= 180")
    private Double destLongitude;

    @Pattern(regexp = "^(CREATED|IN_TRANSIT|DELIVERED|CANCELLED)$", message = "El estado debe ser CREATED, IN_TRANSIT, DELIVERED o CANCELLED")
    private String status = "CREATED";

    private LocalDateTime estimatedDeliveryTime;

    public ShipmentRequestDto() {
    }

    public ShipmentRequestDto(String trackingNumber, Long vehicleId, String originAddress, Double originLatitude, Double originLongitude, String destAddress, Double destLatitude, Double destLongitude, String status, LocalDateTime estimatedDeliveryTime) {
        this.trackingNumber = trackingNumber;
        this.vehicleId = vehicleId;
        this.originAddress = originAddress;
        this.originLatitude = originLatitude;
        this.originLongitude = originLongitude;
        this.destAddress = destAddress;
        this.destLatitude = destLatitude;
        this.destLongitude = destLongitude;
        this.status = status != null ? status : "CREATED";
        this.estimatedDeliveryTime = estimatedDeliveryTime;
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
}
