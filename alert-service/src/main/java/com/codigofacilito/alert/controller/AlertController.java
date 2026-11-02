package com.codigofacilito.alert.controller;

import com.codigofacilito.alert.dto.AlertRequestDto;
import com.codigofacilito.alert.dto.AlertResponseDto;
import com.codigofacilito.alert.dto.TelemetryEvaluationRequestDto;
import com.codigofacilito.alert.dto.TelemetryEvaluationResponseDto;
import com.codigofacilito.alert.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

/**
 * Controlador REST y SSE para la gestión y detección reactiva de alertas e incidentes de flota.
 */
@RestController
@RequestMapping("/api/v1/alerts")
@Tag(name = "Alertas e Incidentes", description = "Endpoints para la gestión, resolución, evaluación de anomalías y streaming de alertas")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar nueva alerta", description = "Crea manualmente un reporte de alerta e incidente para un vehículo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Alerta creada y transmitida", content = @Content(schema = @Schema(implementation = AlertResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de alerta inválidos")
    })
    public Mono<ResponseEntity<AlertResponseDto>> createAlert(@Valid @RequestBody AlertRequestDto requestDto) {
        return alertService.createAlert(requestDto)
                .map(created -> ResponseEntity
                        .created(URI.create("/api/v1/alerts/" + created.getId()))
                        .body(created));
    }

    @GetMapping
    @Operation(summary = "Listar alertas", description = "Obtiene alertas registradas con filtros opcionales por estado de resolución y severidad")
    @ApiResponse(responseCode = "200", description = "Listado de alertas obtenido")
    public Flux<AlertResponseDto> getAllAlerts(
            @Parameter(description = "Filtro por resolución: true o false")
            @RequestParam(required = false) Boolean resolved,
            @Parameter(description = "Filtro por severidad: LOW, MEDIUM, HIGH, CRITICAL")
            @RequestParam(required = false) String severity) {
        return alertService.getAllAlerts(resolved, severity);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener alerta por ID", description = "Consulta la información detallada de una alerta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alerta encontrada"),
            @ApiResponse(responseCode = "404", description = "Alerta no encontrada")
    })
    public Mono<ResponseEntity<AlertResponseDto>> getAlertById(@PathVariable Long id) {
        return alertService.getAlertById(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/vehicle/{vehicleId}")
    @Operation(summary = "Listar alertas por vehículo", description = "Obtiene el historial cronológico de alertas para un vehículo específico")
    @ApiResponse(responseCode = "200", description = "Historial de alertas del vehículo obtenido")
    public Flux<AlertResponseDto> getAlertsByVehicle(@PathVariable Long vehicleId) {
        return alertService.getAlertsByVehicle(vehicleId);
    }

    @PatchMapping("/{id}/resolve")
    @Operation(summary = "Resolver alerta", description = "Marca una alerta activa como resuelta registrando la hora de atención")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alerta resuelta exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alerta no encontrada")
    })
    public Mono<ResponseEntity<AlertResponseDto>> resolveAlert(@PathVariable Long id) {
        return alertService.resolveAlert(id)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar alerta", description = "Elimina un registro de alerta del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Alerta eliminada"),
            @ApiResponse(responseCode = "404", description = "Alerta no encontrada")
    })
    public Mono<ResponseEntity<Void>> deleteAlert(@PathVariable Long id) {
        return alertService.deleteAlert(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    @PostMapping("/evaluate-telemetry")
    @Operation(summary = "Evaluar telemetría de sensores", description = "Evalúa automáticamente velocidad, batería, combustible y botón de pánico generando alertas inmediatas si se violan las reglas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evaluación de telemetría completada", content = @Content(schema = @Schema(implementation = TelemetryEvaluationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public Mono<ResponseEntity<TelemetryEvaluationResponseDto>> evaluateTelemetry(@Valid @RequestBody TelemetryEvaluationRequestDto evaluationRequest) {
        return alertService.evaluateTelemetry(evaluationRequest)
                .map(ResponseEntity::ok);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Streaming reactivo de alertas (Server-Sent Events)", description = "Canal SSE continuo para recibir notificaciones en tiempo real cuando se disparen incidentes")
    @ApiResponse(responseCode = "200", description = "Canal SSE establecido")
    public Flux<AlertResponseDto> streamAlerts() {
        return alertService.streamAlerts();
    }
}
