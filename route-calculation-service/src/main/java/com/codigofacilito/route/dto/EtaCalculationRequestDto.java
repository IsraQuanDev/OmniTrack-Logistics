package com.codigofacilito.route.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para la solicitud de cálculo de ruta y ETA.
 */
public class EtaCalculationRequestDto {

    @NotNull(message = "El identificador del vehículo es obligatorio")
    private Long vehicleId;

    private Double originLatitude;
    private Double originLongitude;

    @NotNull(message = "La latitud actual es obligatoria")
    @DecimalMin(value = "-90.0", message = "La latitud actual debe ser >= -90.0")
    @DecimalMax(value = "90.0", message = "La latitud actual debe ser <= 90.0")
    private Double currentLatitude;

    @NotNull(message = "La longitud actual es obligatoria")
    @DecimalMin(value = "-180.0", message = "La longitud actual debe ser >= -180.0")
    @DecimalMax(value = "180.0", message = "La longitud actual debe ser <= 180.0")
    private Double currentLongitude;

    @NotNull(message = "La latitud de destino es obligatoria")
    @DecimalMin(value = "-90.0", message = "La latitud de destino debe ser >= -90.0")
    @DecimalMax(value = "90.0", message = "La latitud de destino debe ser <= 90.0")
    private Double destLatitude;

    @NotNull(message = "La longitud de destino es obligatoria")
    @DecimalMin(value = "-180.0", message = "La longitud de destino debe ser >= -180.0")
    @DecimalMax(value = "180.0", message = "La longitud de destino debe ser <= 180.0")
    private Double destLongitude;

    @Min(value = 1, message = "La velocidad en km/h debe ser mayor o igual a 1 para estimar el ETA")
    private Double currentSpeedKmh = 60.0; // Velocidad por defecto en autopista si está detenido momentáneamente

    public EtaCalculationRequestDto() {
    }

    public EtaCalculationRequestDto(Long vehicleId, Double originLatitude, Double originLongitude, Double currentLatitude, Double currentLongitude, Double destLatitude, Double destLongitude, Double currentSpeedKmh) {
        this.vehicleId = vehicleId;
        this.originLatitude = originLatitude;
        this.originLongitude = originLongitude;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.destLatitude = destLatitude;
        this.destLongitude = destLongitude;
        this.currentSpeedKmh = currentSpeedKmh != null && currentSpeedKmh > 0 ? currentSpeedKmh : 60.0;
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
}
