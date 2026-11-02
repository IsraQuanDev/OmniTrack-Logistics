package com.codigofacilito.gps.controller;

import com.codigofacilito.gps.dto.BatchTelemetryRequestDto;
import com.codigofacilito.gps.dto.BatchTelemetryResponseDto;
import com.codigofacilito.gps.dto.TelemetryRequestDto;
import com.codigofacilito.gps.dto.TelemetryResponseDto;
import com.codigofacilito.gps.service.TelemetryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;

/**
 * Controlador REST y SSE para la ingesta y streaming de telemetría GPS.
 */
@RestController
@RequestMapping("/api/v1/telemetry")
@Tag(name = "Telemetría GPS", description = "Endpoints para ingesta individual, masiva (batch) y consulta de telemetría GPS")
public class TelemetryController {

    private final TelemetryService telemetryService;

    public TelemetryController(TelemetryService telemetryService) {
        this.telemetryService = telemetryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ingestar coordenada GPS individual", description = "Recibe un reporte de telemetría y lo difunde reactivamente a suscriptores")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Telemetría registrada y difundida exitosamente", content = @Content(schema = @Schema(implementation = TelemetryResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Coordenadas o datos de telemetría inválidos")
    })
    public Mono<ResponseEntity<TelemetryResponseDto>> ingestTelemetry(@Valid @RequestBody TelemetryRequestDto requestDto) {
        return telemetryService.ingestTelemetry(requestDto)
                .map(created -> ResponseEntity
                        .created(URI.create("/api/v1/telemetry/latest/" + created.getVehicleId()))
                        .body(created));
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ingesta masiva por lote", description = "Procesa una colección de coordenadas de telemetría de forma reactiva no bloqueante")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lote de telemetría procesado", content = @Content(schema = @Schema(implementation = BatchTelemetryResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Lote vacío o datos inválidos")
    })
    public Mono<ResponseEntity<BatchTelemetryResponseDto>> ingestBatch(@Valid @RequestBody BatchTelemetryRequestDto batchRequest) {
        return telemetryService.ingestBatch(batchRequest)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @GetMapping("/latest/{vehicleId}")
    @Operation(summary = "Obtener última posición conocida", description = "Consulta la coordenada y lectura de sensores más reciente de un vehículo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Última posición obtenida"),
            @ApiResponse(responseCode = "404", description = "No se encontraron lecturas para el vehículo")
    })
    public Mono<ResponseEntity<TelemetryResponseDto>> getLatestPosition(@PathVariable Long vehicleId) {
        return telemetryService.getLatestTelemetry(vehicleId)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/history/{vehicleId}")
    @Operation(summary = "Historial de trayectorias", description = "Consulta el historial de coordenadas de un vehículo en un intervalo temporal")
    @ApiResponse(responseCode = "200", description = "Historial de coordenadas obtenido")
    public Flux<TelemetryResponseDto> getHistory(
            @PathVariable Long vehicleId,
            @Parameter(description = "Fecha y hora inicial en formato ISO (ej. 2026-11-01T00:00:00)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "Fecha y hora final en formato ISO (ej. 2026-11-02T23:59:59)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return telemetryService.getTelemetryHistory(vehicleId, start, end);
    }

    @GetMapping(value = "/stream/{vehicleId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Streaming en tiempo real (Server-Sent Events)", description = "Establece un canal SSE reactivo para recibir las coordenadas en vivo del vehículo")
    @ApiResponse(responseCode = "200", description = "Conexión SSE establecida para streaming de telemetría")
    public Flux<TelemetryResponseDto> streamVehicleTelemetry(@PathVariable Long vehicleId) {
        return telemetryService.streamVehicleTelemetry(vehicleId);
    }
}
