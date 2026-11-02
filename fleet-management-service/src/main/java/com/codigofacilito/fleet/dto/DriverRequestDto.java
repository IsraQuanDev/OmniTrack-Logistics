package com.codigofacilito.fleet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para el registro y actualización de conductores (Drivers).
 */
public class DriverRequestDto {

    @NotBlank(message = "El nombre completo del conductor es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String fullName;

    @NotBlank(message = "El número de licencia es obligatorio")
    @Size(min = 5, max = 50, message = "La licencia debe tener entre 5 y 50 caracteres")
    private String licenseNumber;

    @NotBlank(message = "El número telefónico es obligatorio")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "El teléfono debe contener entre 10 y 15 dígitos numéricos válidos")
    private String phone;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato de correo electrónico no es válido")
    private String email;

    private Boolean active = true;

    public DriverRequestDto() {
    }

    public DriverRequestDto(String fullName, String licenseNumber, String phone, String email, Boolean active) {
        this.fullName = fullName;
        this.licenseNumber = licenseNumber;
        this.phone = phone;
        this.email = email;
        this.active = active != null ? active : true;
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
}
