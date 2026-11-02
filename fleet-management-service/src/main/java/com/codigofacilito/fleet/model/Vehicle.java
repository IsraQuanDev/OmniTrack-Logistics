package com.codigofacilito.fleet.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad de dominio Vehicle (Vehículo / Unidad de transporte de la flota).
 */
@Table("vehicles")
public class Vehicle {

    @Id
    private Long id;

    @Column("vin")
    private String vin; // Vehicle Identification Number

    @Column("license_plate")
    private String licensePlate;

    @Column("brand")
    private String brand;

    @Column("model")
    private String model;

    @Column("year_manufactured")
    private Integer yearManufactured;

    @Column("status")
    private String status; // ACTIVE, INACTIVE, IN_TRANSIT, MAINTENANCE

    @Column("capacity_kg")
    private Double capacityKg;

    @Column("assigned_driver_id")
    private Long assignedDriverId;

    @Column("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Vehicle() {
    }

    public Vehicle(Long id, String vin, String licensePlate, String brand, String model, Integer yearManufactured, String status, Double capacityKg, Long assignedDriverId, LocalDateTime createdAt) {
        this.id = id;
        this.vin = vin;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.yearManufactured = yearManufactured;
        this.status = status;
        this.capacityKg = capacityKg;
        this.assignedDriverId = assignedDriverId;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
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
