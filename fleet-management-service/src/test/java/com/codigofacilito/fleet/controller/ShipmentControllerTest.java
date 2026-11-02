package com.codigofacilito.fleet.controller;

import com.codigofacilito.fleet.config.SecurityConfig;
import com.codigofacilito.fleet.dto.ShipmentRequestDto;
import com.codigofacilito.fleet.dto.ShipmentResponseDto;
import com.codigofacilito.fleet.service.ShipmentService;
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

@WebFluxTest(controllers = ShipmentController.class)
@Import(SecurityConfig.class)
class ShipmentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ShipmentService shipmentService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/v1/shipments - Crear nuevo envío")
    void testCreateShipment_Success() {
        ShipmentRequestDto request = new ShipmentRequestDto("TRK-1001", 1L, "Origen CDMX", 19.43, -99.13, "Destino MTY", 25.68, -100.31, "CREATED", null);
        ShipmentResponseDto response = new ShipmentResponseDto(1L, "TRK-1001", 1L, "Origen CDMX", 19.43, -99.13, "Destino MTY", 25.68, -100.31, "CREATED", null, LocalDateTime.now());

        Mockito.when(shipmentService.createShipment(any(ShipmentRequestDto.class))).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/v1/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.trackingNumber").isEqualTo("TRK-1001");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /api/v1/shipments - Listar envíos")
    void testGetAllShipments() {
        ShipmentResponseDto s1 = new ShipmentResponseDto(1L, "TRK-1001", 1L, "Origen CDMX", 19.43, -99.13, "Destino MTY", 25.68, -100.31, "CREATED", null, LocalDateTime.now());
        Mockito.when(shipmentService.getAllShipments(null)).thenReturn(Flux.just(s1));

        webTestClient.get()
                .uri("/api/v1/shipments")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ShipmentResponseDto.class)
                .hasSize(1);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /api/v1/shipments/{id} - Obtener por ID")
    void testGetShipmentById_Success() {
        ShipmentResponseDto s1 = new ShipmentResponseDto(1L, "TRK-1001", 1L, "Origen CDMX", 19.43, -99.13, "Destino MTY", 25.68, -100.31, "CREATED", null, LocalDateTime.now());
        Mockito.when(shipmentService.getShipmentById(1L)).thenReturn(Mono.just(s1));

        webTestClient.get()
                .uri("/api/v1/shipments/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /api/v1/shipments/tracking/{code} - Obtener por tracking")
    void testGetShipmentByTrackingNumber_Success() {
        ShipmentResponseDto s1 = new ShipmentResponseDto(1L, "TRK-1001", 1L, "Origen CDMX", 19.43, -99.13, "Destino MTY", 25.68, -100.31, "CREATED", null, LocalDateTime.now());
        Mockito.when(shipmentService.getShipmentByTrackingNumber("TRK-1001")).thenReturn(Mono.just(s1));

        webTestClient.get()
                .uri("/api/v1/shipments/tracking/TRK-1001")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.trackingNumber").isEqualTo("TRK-1001");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("PATCH /api/v1/shipments/{id}/status - Actualizar estado del envío")
    void testUpdateShipmentStatus_Success() {
        ShipmentResponseDto s1 = new ShipmentResponseDto(1L, "TRK-1001", 1L, "Origen CDMX", 19.43, -99.13, "Destino MTY", 25.68, -100.31, "DELIVERED", null, LocalDateTime.now());
        Mockito.when(shipmentService.updateShipmentStatus(1L, "DELIVERED")).thenReturn(Mono.just(s1));

        webTestClient.patch()
                .uri("/api/v1/shipments/1/status?status=DELIVERED")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("DELIVERED");
    }
}
