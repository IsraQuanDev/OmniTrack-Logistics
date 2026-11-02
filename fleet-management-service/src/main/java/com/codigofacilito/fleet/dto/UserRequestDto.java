package com.codigofacilito.fleet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para la creación y actualización de usuarios del sistema.
 * Incluye validaciones robustas de entrada (Punto Extra 1).
 */
public class UserRequestDto {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato de correo electrónico no es válido")
    private String email;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 2, max = 150, message = "El nombre completo debe tener entre 2 y 150 caracteres")
    private String fullName;

    @Pattern(regexp = "^(ROLE_USER|ROLE_ADMIN|ROLE_OPERATOR)$", message = "El rol debe ser ROLE_USER, ROLE_ADMIN o ROLE_OPERATOR")
    private String role = "ROLE_USER";

    private Boolean active = true;

    public UserRequestDto() {
    }

    public UserRequestDto(String username, String password, String email, String fullName, String role, Boolean active) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.role = role != null ? role : "ROLE_USER";
        this.active = active != null ? active : true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
