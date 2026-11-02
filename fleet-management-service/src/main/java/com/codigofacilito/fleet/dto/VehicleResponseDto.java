package com.codigofacilito.fleet.dto;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para Vehicle, con soporte de hipervínculos HATEOAS.
 */
public class VehicleResponseDto extends RepresentationModel<VehicleResponseDto> {

    private Long id;
    private String vin;
    private String licensePlate;
    private String brand;
    private String model;
    private Integer yearManufactured;
    private String status;
    private Double capacityKg;
    private Long assignedDriverId;
    private LocalDateTime createdAt;

    public VehicleResponseDto() {
    }

    public VehicleResponseDto(Long id, String vin, String licensePlate, String brand, String model, Integer yearManufactured, String status, Double capacityKg, Long assignedDriverId, LocalDateTime createdAt) {
        this.id = id;
        this.vin = vin;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.yearManufactured = yearManufactured;
        this.status = status;
        this.capacityKg = capacityKg;
        this.assignedDriverId = assignedDriverId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYearManufactured() {
        return yearManufactured;
    }

    public void setYearManufactured(Integer yearManufactured) {
        this.yearManufactured = yearManufactured;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getCapacityKg() {
        return capacityKg;
    }

    public void setCapacityKg(Double capacityKg) {
        this.capacityKg = capacityKg;
    }

    public Long getAssignedDriverId() {
        return assignedDriverId;
    }

    public void setAssignedDriverId(Long assignedDriverId) {
        this.assignedDriverId = assignedDriverId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
