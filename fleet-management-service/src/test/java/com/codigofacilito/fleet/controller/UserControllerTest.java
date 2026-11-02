package com.codigofacilito.fleet.controller;

import com.codigofacilito.fleet.config.SecurityConfig;
import com.codigofacilito.fleet.dto.UserRequestDto;
import com.codigofacilito.fleet.dto.UserResponseDto;
import com.codigofacilito.fleet.exception.ResourceNotFoundException;
import com.codigofacilito.fleet.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebFluxTest(controllers = UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/v1/users - Éxito al crear usuario")
    void testCreateUser_Success() {
        UserRequestDto request = new UserRequestDto("nuevo_usuario", "password123", "nuevo@example.com", "Nuevo Usuario", "ROLE_USER", true);
        UserResponseDto response = new UserResponseDto(10L, "nuevo_usuario", "nuevo@example.com", "Nuevo Usuario", "ROLE_USER", true, LocalDateTime.now());

        Mockito.when(userService.createUser(any(UserRequestDto.class))).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(10)
                .jsonPath("$.username").isEqualTo("nuevo_usuario")
                .jsonPath("$.email").isEqualTo("nuevo@example.com");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/v1/users - Error 400 por validación de campos")
    void testCreateUser_ValidationError() {
        UserRequestDto invalidRequest = new UserRequestDto("", "123", "email-invalido", "", null, null);

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.fieldErrors.username").exists();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("GET /api/v1/users - Listar todos los usuarios")
    void testGetAllUsers() {
        UserResponseDto user1 = new UserResponseDto(1L, "admin", "admin@example.com", "Admin User", "ROLE_ADMIN", true, LocalDateTime.now());
        UserResponseDto user2 = new UserResponseDto(2L, "user1", "user1@example.com", "Standard User", "ROLE_USER", true, LocalDateTime.now());

        Mockito.when(userService.getAllUsers()).thenReturn(Flux.just(user1, user2));

        webTestClient.get()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponseDto.class)
                .hasSize(2);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("GET /api/v1/users/{id} - Obtener usuario existente")
    void testGetUserById_Success() {
        UserResponseDto user = new UserResponseDto(1L, "admin", "admin@example.com", "Admin User", "ROLE_ADMIN", true, LocalDateTime.now());
        Mockito.when(userService.getUserById(1L)).thenReturn(Mono.just(user));

        webTestClient.get()
                .uri("/api/v1/users/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("admin");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("GET /api/v1/users/{id} - Error 404 cuando no existe")
    void testGetUserById_NotFound() {
        Mockito.when(userService.getUserById(999L))
                .thenReturn(Mono.error(new ResourceNotFoundException("Usuario no encontrado con ID: 999")));

        webTestClient.get()
                .uri("/api/v1/users/999")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("PUT /api/v1/users/{id} - Actualizar usuario")
    void testUpdateUser_Success() {
        UserRequestDto request = new UserRequestDto("admin_updated", "password123", "admin_up@example.com", "Admin Actualizado", "ROLE_ADMIN", true);
        UserResponseDto response = new UserResponseDto(1L, "admin_updated", "admin_up@example.com", "Admin Actualizado", "ROLE_ADMIN", true, LocalDateTime.now());

        Mockito.when(userService.updateUser(eq(1L), any(UserRequestDto.class))).thenReturn(Mono.just(response));

        webTestClient.put()
                .uri("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.fullName").isEqualTo("Admin Actualizado");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("DELETE /api/v1/users/{id} - Eliminar usuario")
    void testDeleteUser_Success() {
        Mockito.when(userService.deleteUser(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/users/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
