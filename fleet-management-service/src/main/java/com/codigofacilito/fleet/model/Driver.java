package com.codigofacilito.fleet.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad de dominio Driver (Conductor / Transportista).
 */
@Table("drivers")
public class Driver {

    @Id
    private Long id;

    @Column("full_name")
    private String fullName;

    @Column("license_number")
    private String licenseNumber;

    @Column("phone")
    private String phone;

    @Column("email")
    private String email;

    @Column("active")
    private Boolean active = true;

    @Column("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Driver() {
    }

    public Driver(Long id, String fullName, String licenseNumber, String phone, String email, Boolean active, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.licenseNumber = licenseNumber;
        this.phone = phone;
        this.email = email;
        this.active = active != null ? active : true;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
