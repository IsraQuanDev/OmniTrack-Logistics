package com.codigofacilito.fleet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para la creación y actualización de unidades de transporte (Vehicles).
 */
public class VehicleRequestDto {

    @NotBlank(message = "El número VIN es obligatorio")
    @Size(min = 17, max = 17, message = "El número VIN debe tener exactamente 17 caracteres")
    private String vin;

    @NotBlank(message = "La placa de matrícula es obligatoria")
    @Pattern(regexp = "^[A-Z0-9-]{6,10}$", message = "La placa debe tener entre 6 y 10 caracteres alfanuméricos")
    private String licensePlate;

    @NotBlank(message = "La marca del vehículo es obligatoria")
    @Size(min = 2, max = 100, message = "La marca debe tener entre 2 y 100 caracteres")
    private String brand;

    @NotBlank(message = "El modelo del vehículo es obligatorio")
    @Size(min = 1, max = 100, message = "El modelo debe tener entre 1 y 100 caracteres")
    private String model;

    @NotNull(message = "El año de fabricación es obligatorio")
    @Min(value = 1990, message = "El año debe ser igual o posterior a 1990")
    @Max(value = 2030, message = "El año no puede ser superior a 2030")
    private Integer yearManufactured;

    @Pattern(regexp = "^(ACTIVE|INACTIVE|IN_TRANSIT|MAINTENANCE)$", message = "El estado debe ser ACTIVE, INACTIVE, IN_TRANSIT o MAINTENANCE")
    private String status = "ACTIVE";

    @NotNull(message = "La capacidad de carga en kg es obligatoria")
    @DecimalMin(value = "100.0", message = "La capacidad mínima de carga debe ser de al menos 100 kg")
    private Double capacityKg;

    private Long assignedDriverId;

    public VehicleRequestDto() {
    }

    public VehicleRequestDto(String vin, String licensePlate, String brand, String model, Integer yearManufactured, String status, Double capacityKg, Long assignedDriverId) {
        this.vin = vin;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.yearManufactured = yearManufactured;
        this.status = status != null ? status : "ACTIVE";
        this.capacityKg = capacityKg;
        this.assignedDriverId = assignedDriverId;
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
}
