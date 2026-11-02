package com.codigofacilito.route.controller;

import com.codigofacilito.route.config.SecurityConfig;
import com.codigofacilito.route.dto.GeofenceCheckRequestDto;
import com.codigofacilito.route.dto.GeofenceCheckResponseDto;
import com.codigofacilito.route.dto.GeofenceRequestDto;
import com.codigofacilito.route.dto.GeofenceResponseDto;
import com.codigofacilito.route.service.GeofenceService;
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

@WebFluxTest(controllers = GeofenceController.class)
@Import(SecurityConfig.class)
class GeofenceControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GeofenceService geofenceService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/v1/geofences - Crear geocerca")
    void testCreateGeofence_Success() {
        GeofenceRequestDto request = new GeofenceRequestDto("Zona Norte", "Parque Logístico", 19.50, -99.20, 5000.0, true, 1L);
        GeofenceResponseDto response = new GeofenceResponseDto(1L, "Zona Norte", "Parque Logístico", 19.50, -99.20, 5000.0, true, 1L, LocalDateTime.now());

        Mockito.when(geofenceService.createGeofence(any(GeofenceRequestDto.class))).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/v1/geofences")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Zona Norte");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /api/v1/geofences - Listar todas")
    void testGetAllGeofences() {
        GeofenceResponseDto g1 = new GeofenceResponseDto(1L, "Zona Norte", "Parque Logístico", 19.50, -99.20, 5000.0, true, 1L, LocalDateTime.now());
        Mockito.when(geofenceService.getAllGeofences()).thenReturn(Flux.just(g1));

        webTestClient.get()
                .uri("/api/v1/geofences")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GeofenceResponseDto.class)
                .hasSize(1);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /api/v1/geofences/{id} - Obtener por ID")
    void testGetGeofenceById_Success() {
        GeofenceResponseDto g1 = new GeofenceResponseDto(1L, "Zona Norte", "Parque Logístico", 19.50, -99.20, 5000.0, true, 1L, LocalDateTime.now());
        Mockito.when(geofenceService.getGeofenceById(1L)).thenReturn(Mono.just(g1));

        webTestClient.get()
                .uri("/api/v1/geofences/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.radiusMeters").isEqualTo(5000.0);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("PUT /api/v1/geofences/{id} - Actualizar geocerca")
    void testUpdateGeofence_Success() {
        GeofenceRequestDto request = new GeofenceRequestDto("Zona Norte Modificada", "Parque Logístico", 19.50, -99.20, 6000.0, true, 1L);
        GeofenceResponseDto response = new GeofenceResponseDto(1L, "Zona Norte Modificada", "Parque Logístico", 19.50, -99.20, 6000.0, true, 1L, LocalDateTime.now());

        Mockito.when(geofenceService.updateGeofence(eq(1L), any(GeofenceRequestDto.class))).thenReturn(Mono.just(response));

        webTestClient.put()
                .uri("/api/v1/geofences/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Zona Norte Modificada");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("DELETE /api/v1/geofences/{id} - Eliminar geocerca")
    void testDeleteGeofence_Success() {
        Mockito.when(geofenceService.deleteGeofence(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/geofences/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /api/v1/geofences/check - Verificar si coordenada está dentro")
    void testCheckPoint_Success() {
        GeofenceCheckRequestDto checkRequest = new GeofenceCheckRequestDto(1L, 19.501, -99.201);
        GeofenceCheckResponseDto checkResponse = new GeofenceCheckResponseDto(1L, "Zona Norte", true, "INSIDE", 150.0, 5000.0);

        Mockito.when(geofenceService.checkPointInGeofence(any(GeofenceCheckRequestDto.class))).thenReturn(Mono.just(checkResponse));

        webTestClient.post()
                .uri("/api/v1/geofences/check")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(checkRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.isInside").isEqualTo(true)
                .jsonPath("$.status").isEqualTo("INSIDE");
    }
}
