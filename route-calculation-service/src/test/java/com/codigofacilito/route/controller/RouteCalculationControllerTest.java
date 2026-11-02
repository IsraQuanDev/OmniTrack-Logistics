package com.codigofacilito.route.controller;

import com.codigofacilito.route.config.SecurityConfig;
import com.codigofacilito.route.dto.EtaCalculationRequestDto;
import com.codigofacilito.route.dto.EtaCalculationResponseDto;
import com.codigofacilito.route.service.RouteCalculationService;
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

@WebFluxTest(controllers = RouteCalculationController.class)
@Import(SecurityConfig.class)
class RouteCalculationControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RouteCalculationService routeCalculationService;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /api/v1/routes/calculate-eta - Éxito cálculo de ETA")
    void testCalculateEta_Success() {
        EtaCalculationRequestDto request = new EtaCalculationRequestDto(1L, 19.43, -99.13, 19.50, -99.20, 20.65, -103.35, 80.0);
        EtaCalculationResponseDto response = new EtaCalculationResponseDto(1L, 1L, 19.50, -99.20, 20.65, -103.35, 80.0, 450.5, 337.8, "5 h 38 min", LocalDateTime.now().plusHours(5), LocalDateTime.now());

        Mockito.when(routeCalculationService.calculateEta(any(EtaCalculationRequestDto.class))).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/v1/routes/calculate-eta")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.distanceRemainingKm").isEqualTo(450.5)
                .jsonPath("$.etaFormatted").isEqualTo("5 h 38 min");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /api/v1/routes/calculate-eta - Error de validación en coordenadas")
    void testCalculateEta_ValidationError() {
        EtaCalculationRequestDto invalidRequest = new EtaCalculationRequestDto(null, null, null, 120.0, -99.20, 20.65, -103.35, 0.0);

        webTestClient.post()
                .uri("/api/v1/routes/calculate-eta")
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
    @DisplayName("GET /api/v1/routes/history/{vehicleId} - Historial de cálculos")
    void testGetHistory_Success() {
        EtaCalculationResponseDto response = new EtaCalculationResponseDto(1L, 1L, 19.50, -99.20, 20.65, -103.35, 80.0, 450.5, 337.8, "5 h 38 min", LocalDateTime.now().plusHours(5), LocalDateTime.now());
        Mockito.when(routeCalculationService.getCalculationHistory(1L)).thenReturn(Flux.just(response));

        webTestClient.get()
                .uri("/api/v1/routes/history/1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EtaCalculationResponseDto.class)
                .hasSize(1);
    }
}
