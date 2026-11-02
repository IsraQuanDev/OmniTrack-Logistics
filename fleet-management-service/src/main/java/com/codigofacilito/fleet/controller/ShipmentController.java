package com.codigofacilito.fleet.controller;

import com.codigofacilito.fleet.dto.ShipmentRequestDto;
import com.codigofacilito.fleet.dto.ShipmentResponseDto;
import com.codigofacilito.fleet.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
 * Controlador REST para el despacho y seguimiento de Envíos (Shipments).
 */
@RestController
@RequestMapping("/api/v1/shipments")
@Tag(name = "Envíos y Despachos", description = "Endpoints para la gestión logística de viajes, origen, destino y guías de carga")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear nuevo envío", description = "Despacha un nuevo envío asignado a un vehículo con coordenadas de origen y destino")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Envío registrado", content = @Content(schema = @Schema(implementation = ShipmentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Vehículo asignado no encontrado"),
            @ApiResponse(responseCode = "409", description = "Número de guía duplicado")
    })
    public Mono<ResponseEntity<ShipmentResponseDto>> createShipment(@Valid @RequestBody ShipmentRequestDto requestDto) {
        return shipmentService.createShipment(requestDto)
                .map(created -> ResponseEntity
                        .created(URI.create("/api/v1/shipments/" + created.getId()))
                        .body(created));
    }

    @GetMapping
    @Operation(summary = "Listar envíos", description = "Obtiene los envíos registrados, con filtro opcional por estado")
    @ApiResponse(responseCode = "200", description = "Listado de envíos obtenido")
    public Flux<ShipmentResponseDto> getAllShipments(
            @Parameter(description = "Filtro opcional por estado: CREATED, IN_TRANSIT, DELIVERED, CANCELLED")
            @RequestParam(required = false) String status) {
        return shipmentService.getAllShipments(status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener envío por ID", description = "Consulta la información de un envío específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Envío encontrado"),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    public Mono<ResponseEntity<ShipmentResponseDto>> getShipmentById(@PathVariable Long id) {
        return shipmentService.getShipmentById(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/tracking/{trackingNumber}")
    @Operation(summary = "Obtener envío por número de rastreo", description = "Consulta el envío mediante el código de tracking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Envío encontrado"),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    public Mono<ResponseEntity<ShipmentResponseDto>> getShipmentByTrackingNumber(@PathVariable String trackingNumber) {
        return shipmentService.getShipmentByTrackingNumber(trackingNumber)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Actualizar estado del envío", description = "Actualiza el ciclo de vida del envío: IN_TRANSIT, DELIVERED, CANCELLED")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado del envío actualizado"),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    public Mono<ResponseEntity<ShipmentResponseDto>> updateShipmentStatus(
            @PathVariable Long id,
            @Parameter(description = "Nuevo estado: CREATED, IN_TRANSIT, DELIVERED, CANCELLED")
            @RequestParam String status) {
        return shipmentService.updateShipmentStatus(id, status)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar envío", description = "Elimina un registro de envío (requiere rol ROLE_ADMIN)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Envío eliminado"),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    public Mono<ResponseEntity<Void>> deleteShipment(@PathVariable Long id) {
        return shipmentService.deleteShipment(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
}
