package com.codigofacilito.fleet.controller;

import com.codigofacilito.fleet.config.SecurityConfig;
import com.codigofacilito.fleet.dto.VehicleRequestDto;
import com.codigofacilito.fleet.dto.VehicleResponseDto;
import com.codigofacilito.fleet.exception.ResourceNotFoundException;
import com.codigofacilito.fleet.service.VehicleService;
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

@WebFluxTest(controllers = VehicleController.class)
@Import(SecurityConfig.class)
class VehicleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private VehicleService vehicleService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/v1/vehicles - Éxito al registrar vehículo")
    void testCreateVehicle_Success() {
        VehicleRequestDto request = new VehicleRequestDto("12345678901234567", "ABC-1234", "Volvo", "FH16", 2023, "ACTIVE", 25000.0, 1L);
        VehicleResponseDto response = new VehicleResponseDto(1L, "12345678901234567", "ABC-1234", "Volvo", "FH16", 2023, "ACTIVE", 25000.0, 1L, LocalDateTime.now());

        Mockito.when(vehicleService.createVehicle(any(VehicleRequestDto.class))).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.licensePlate").isEqualTo("ABC-1234");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /api/v1/vehicles - Listar vehículos")
    void testGetAllVehicles() {
        VehicleResponseDto v1 = new VehicleResponseDto(1L, "12345678901234567", "ABC-1234", "Volvo", "FH16", 2023, "ACTIVE", 25000.0, 1L, LocalDateTime.now());
        Mockito.when(vehicleService.getAllVehicles(null)).thenReturn(Flux.just(v1));

        webTestClient.get()
                .uri("/api/v1/vehicles")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VehicleResponseDto.class)
                .hasSize(1);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /api/v1/vehicles/{id} - Obtener por ID")
    void testGetVehicleById_Success() {
        VehicleResponseDto v1 = new VehicleResponseDto(1L, "12345678901234567", "ABC-1234", "Volvo", "FH16", 2023, "ACTIVE", 25000.0, 1L, LocalDateTime.now());
        Mockito.when(vehicleService.getVehicleById(1L)).thenReturn(Mono.just(v1));

        webTestClient.get()
                .uri("/api/v1/vehicles/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.brand").isEqualTo("Volvo");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("PATCH /api/v1/vehicles/{id}/status - Actualizar estado")
    void testUpdateVehicleStatus_Success() {
        VehicleResponseDto response = new VehicleResponseDto(1L, "12345678901234567", "ABC-1234", "Volvo", "FH16", 2023, "MAINTENANCE", 25000.0, 1L, LocalDateTime.now());
        Mockito.when(vehicleService.updateVehicleStatus(1L, "MAINTENANCE")).thenReturn(Mono.just(response));

        webTestClient.patch()
                .uri("/api/v1/vehicles/1/status?status=MAINTENANCE")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("MAINTENANCE");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("DELETE /api/v1/vehicles/{id} - Eliminar vehículo")
    void testDeleteVehicle_Success() {
        Mockito.when(vehicleService.deleteVehicle(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/vehicles/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
