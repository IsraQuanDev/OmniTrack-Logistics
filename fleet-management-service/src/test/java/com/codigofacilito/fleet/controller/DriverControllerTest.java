package com.codigofacilito.fleet.controller;

import com.codigofacilito.fleet.config.SecurityConfig;
import com.codigofacilito.fleet.dto.DriverRequestDto;
import com.codigofacilito.fleet.dto.DriverResponseDto;
import com.codigofacilito.fleet.service.DriverService;
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

@WebFluxTest(controllers = DriverController.class)
@Import(SecurityConfig.class)
class DriverControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DriverService driverService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/v1/drivers - Registrar conductor")
    void testCreateDriver_Success() {
        DriverRequestDto request = new DriverRequestDto("Roberto Gomez", "LIC-MX-11223", "+525544332211", "roberto@example.com", true);
        DriverResponseDto response = new DriverResponseDto(1L, "Roberto Gomez", "LIC-MX-11223", "+525544332211", "roberto@example.com", true, LocalDateTime.now());

        Mockito.when(driverService.createDriver(any(DriverRequestDto.class))).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/v1/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.fullName").isEqualTo("Roberto Gomez")
                .jsonPath("$.licenseNumber").isEqualTo("LIC-MX-11223");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /api/v1/drivers - Listar conductores")
    void testGetAllDrivers() {
        DriverResponseDto d1 = new DriverResponseDto(1L, "Roberto Gomez", "LIC-MX-11223", "+525544332211", "roberto@example.com", true, LocalDateTime.now());
        Mockito.when(driverService.getAllDrivers()).thenReturn(Flux.just(d1));

        webTestClient.get()
                .uri("/api/v1/drivers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DriverResponseDto.class)
                .hasSize(1);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /api/v1/drivers/{id} - Obtener por ID")
    void testGetDriverById_Success() {
        DriverResponseDto d1 = new DriverResponseDto(1L, "Roberto Gomez", "LIC-MX-11223", "+525544332211", "roberto@example.com", true, LocalDateTime.now());
        Mockito.when(driverService.getDriverById(1L)).thenReturn(Mono.just(d1));

        webTestClient.get()
                .uri("/api/v1/drivers/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.licenseNumber").isEqualTo("LIC-MX-11223");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("DELETE /api/v1/drivers/{id} - Eliminar conductor")
    void testDeleteDriver_Success() {
        Mockito.when(driverService.deleteDriver(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/drivers/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
