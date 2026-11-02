package com.codigofacilito.alert.controller;

import com.codigofacilito.alert.config.SecurityConfig;
import com.codigofacilito.alert.dto.AlertRequestDto;
import com.codigofacilito.alert.dto.AlertResponseDto;
import com.codigofacilito.alert.dto.TelemetryEvaluationRequestDto;
import com.codigofacilito.alert.dto.TelemetryEvaluationResponseDto;
import com.codigofacilito.alert.service.AlertService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebFluxTest(controllers = AlertController.class)
@Import(SecurityConfig.class)
class AlertControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AlertService alertService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/v1/alerts - Crear alerta")
    void testCreateAlert_Success() {
        AlertRequestDto request = new AlertRequestDto(1L, "SPEED_LIMIT_EXCEEDED", "HIGH", "Exceso de velocidad: 110 km/h", 19.43, -99.13, 110.0, 95.0);
        AlertResponseDto response = new AlertResponseDto(1L, 1L, "SPEED_LIMIT_EXCEEDED", "HIGH", "Exceso de velocidad: 110 km/h", 19.43, -99.13, 110.0, 95.0, false, null, LocalDateTime.now());

        Mockito.when(alertService.createAlert(any(AlertRequestDto.class))).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/v1/alerts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.alertType").isEqualTo("SPEED_LIMIT_EXCEEDED");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/v1/alerts - Error de validación en tipo de alerta")
    void testCreateAlert_ValidationError() {
        AlertRequestDto invalidRequest = new AlertRequestDto(null, "TIPO_INVALIDO", "SEVERIDAD_FALSA", "", null, null, null, null);

        webTestClient.post()
                .uri("/api/v1/alerts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.fieldErrors.vehicleId").exists();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /api/v1/alerts - Listar alertas")
    void testGetAllAlerts() {
        AlertResponseDto a1 = new AlertResponseDto(1L, 1L, "SPEED_LIMIT_EXCEEDED", "HIGH", "Exceso de velocidad: 110 km/h", 19.43, -99.13, 110.0, 95.0, false, null, LocalDateTime.now());
        Mockito.when(alertService.getAllAlerts(null, null)).thenReturn(Flux.just(a1));

        webTestClient.get()
                .uri("/api/v1/alerts")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AlertResponseDto.class)
                .hasSize(1);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /api/v1/alerts/{id} - Obtener por ID")
    void testGetAlertById_Success() {
        AlertResponseDto a1 = new AlertResponseDto(1L, 1L, "SPEED_LIMIT_EXCEEDED", "HIGH", "Exceso de velocidad: 110 km/h", 19.43, -99.13, 110.0, 95.0, false, null, LocalDateTime.now());
        Mockito.when(alertService.getAlertById(1L)).thenReturn(Mono.just(a1));

        webTestClient.get()
                .uri("/api/v1/alerts/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.severity").isEqualTo("HIGH");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /api/v1/alerts/vehicle/{vehicleId} - Alertas por vehículo")
    void testGetAlertsByVehicle_Success() {
        AlertResponseDto a1 = new AlertResponseDto(1L, 1L, "SPEED_LIMIT_EXCEEDED", "HIGH", "Exceso de velocidad: 110 km/h", 19.43, -99.13, 110.0, 95.0, false, null, LocalDateTime.now());
        Mockito.when(alertService.getAlertsByVehicle(1L)).thenReturn(Flux.just(a1));

        webTestClient.get()
                .uri("/api/v1/alerts/vehicle/1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AlertResponseDto.class)
                .hasSize(1);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("PATCH /api/v1/alerts/{id}/resolve - Resolver alerta")
    void testResolveAlert_Success() {
        AlertResponseDto a1 = new AlertResponseDto(1L, 1L, "SPEED_LIMIT_EXCEEDED", "HIGH", "Exceso de velocidad: 110 km/h", 19.43, -99.13, 110.0, 95.0, true, LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(alertService.resolveAlert(1L)).thenReturn(Mono.just(a1));

        webTestClient.patch()
                .uri("/api/v1/alerts/1/resolve")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.resolved").isEqualTo(true);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("DELETE /api/v1/alerts/{id} - Eliminar alerta")
    void testDeleteAlert_Success() {
        Mockito.when(alertService.deleteAlert(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/alerts/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /api/v1/alerts/evaluate-telemetry - Evaluar telemetría y disparar alerta")
    void testEvaluateTelemetry_Success() {
        TelemetryEvaluationRequestDto request = new TelemetryEvaluationRequestDto(1L, 115.0, 19.43, -99.13, 85.0, 60.0, false);
        AlertResponseDto speedAlert = new AlertResponseDto(10L, 1L, "SPEED_LIMIT_EXCEEDED", "HIGH", "Velocidad excesiva detectada: 115.0 km/h", 19.43, -99.13, 115.0, 95.0, false, null, LocalDateTime.now());
        TelemetryEvaluationResponseDto response = new TelemetryEvaluationResponseDto(1L, "ANOMALY_DETECTED", 1, List.of(speedAlert));

        Mockito.when(alertService.evaluateTelemetry(any(TelemetryEvaluationRequestDto.class))).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/v1/alerts/evaluate-telemetry")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("ANOMALY_DETECTED")
                .jsonPath("$.alertsTriggeredCount").isEqualTo(1);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /api/v1/alerts/stream - Streaming SSE de alertas")
    void testStreamAlerts_Success() {
        AlertResponseDto a1 = new AlertResponseDto(1L, 1L, "SPEED_LIMIT_EXCEEDED", "HIGH", "Exceso de velocidad: 110 km/h", 19.43, -99.13, 110.0, 95.0, false, null, LocalDateTime.now());
        Mockito.when(alertService.streamAlerts()).thenReturn(Flux.just(a1));

        webTestClient.get()
                .uri("/api/v1/alerts/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM);
    }
}
