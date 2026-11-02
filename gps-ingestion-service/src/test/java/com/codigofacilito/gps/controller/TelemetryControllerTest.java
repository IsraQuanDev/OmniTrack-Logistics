package com.codigofacilito.gps.controller;

import com.codigofacilito.gps.config.SecurityConfig;
import com.codigofacilito.gps.dto.BatchTelemetryRequestDto;
import com.codigofacilito.gps.dto.BatchTelemetryResponseDto;
import com.codigofacilito.gps.dto.TelemetryRequestDto;
import com.codigofacilito.gps.dto.TelemetryResponseDto;
import com.codigofacilito.gps.exception.ResourceNotFoundException;
import com.codigofacilito.gps.service.TelemetryService;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebFluxTest(controllers = TelemetryController.class)
@Import(SecurityConfig.class)
class TelemetryControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TelemetryService telemetryService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/v1/telemetry - Ingesta individual de telemetría")
    void testIngestTelemetry_Success() {
        TelemetryRequestDto request = new TelemetryRequestDto(1L, "DEV-001", 19.4326, -99.1332, 2240.0, 75.0, 180.0, 95.0, 80.0, LocalDateTime.now());
        TelemetryResponseDto response = new TelemetryResponseDto(1L, 1L, "DEV-001", 19.4326, -99.1332, 2240.0, 75.0, 180.0, 95.0, 80.0, LocalDateTime.now());

        Mockito.when(telemetryService.ingestTelemetry(any(TelemetryRequestDto.class))).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/v1/telemetry")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.vehicleId").isEqualTo(1)
                .jsonPath("$.speedKmh").isEqualTo(75.0);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/v1/telemetry - Error de validación en coordenadas")
    void testIngestTelemetry_ValidationError() {
        TelemetryRequestDto invalidRequest = new TelemetryRequestDto(1L, "DEV-001", 150.0, -99.1332, null, -10.0, null, null, null, null);

        webTestClient.post()
                .uri("/api/v1/telemetry")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.fieldErrors.latitude").exists();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/v1/telemetry/batch - Ingesta masiva por lote")
    void testIngestBatch_Success() {
        TelemetryRequestDto r1 = new TelemetryRequestDto(1L, "DEV-001", 19.43, -99.13, 2240.0, 60.0, 90.0, 90.0, 70.0, LocalDateTime.now());
        BatchTelemetryRequestDto batch = new BatchTelemetryRequestDto(Collections.singletonList(r1));
        BatchTelemetryResponseDto response = new BatchTelemetryResponseDto(1, "BATCH_PROCESSED_SUCCESSFULLY", List.of(100L));

        Mockito.when(telemetryService.ingestBatch(any(BatchTelemetryRequestDto.class))).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/v1/telemetry/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(batch)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.totalIngested").isEqualTo(1)
                .jsonPath("$.status").isEqualTo("BATCH_PROCESSED_SUCCESSFULLY");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /api/v1/telemetry/latest/{vehicleId} - Obtener última posición")
    void testGetLatestPosition_Success() {
        TelemetryResponseDto response = new TelemetryResponseDto(1L, 1L, "DEV-001", 19.4326, -99.1332, 2240.0, 75.0, 180.0, 95.0, 80.0, LocalDateTime.now());
        Mockito.when(telemetryService.getLatestTelemetry(1L)).thenReturn(Mono.just(response));

        webTestClient.get()
                .uri("/api/v1/telemetry/latest/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.deviceId").isEqualTo("DEV-001");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /api/v1/telemetry/latest/{vehicleId} - Error 404 si no hay telemetría")
    void testGetLatestPosition_NotFound() {
        Mockito.when(telemetryService.getLatestTelemetry(99L))
                .thenReturn(Mono.error(new ResourceNotFoundException("No se encontró telemetría para el vehículo con ID: 99")));

        webTestClient.get()
                .uri("/api/v1/telemetry/latest/99")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /api/v1/telemetry/history/{vehicleId} - Historial de telemetría")
    void testGetHistory_Success() {
        TelemetryResponseDto response = new TelemetryResponseDto(1L, 1L, "DEV-001", 19.4326, -99.1332, 2240.0, 75.0, 180.0, 95.0, 80.0, LocalDateTime.now());
        Mockito.when(telemetryService.getTelemetryHistory(eq(1L), any(), any())).thenReturn(Flux.just(response));

        webTestClient.get()
                .uri("/api/v1/telemetry/history/1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TelemetryResponseDto.class)
                .hasSize(1);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /api/v1/telemetry/stream/{vehicleId} - Streaming SSE reactivo")
    void testStreamVehicleTelemetry_Success() {
        TelemetryResponseDto response = new TelemetryResponseDto(1L, 1L, "DEV-001", 19.4326, -99.1332, 2240.0, 75.0, 180.0, 95.0, 80.0, LocalDateTime.now());
        Mockito.when(telemetryService.streamVehicleTelemetry(1L)).thenReturn(Flux.just(response));

        webTestClient.get()
                .uri("/api/v1/telemetry/stream/1")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM);
    }
}
