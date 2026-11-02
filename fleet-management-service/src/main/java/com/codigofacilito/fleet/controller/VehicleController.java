package com.codigofacilito.fleet.controller;

import com.codigofacilito.fleet.dto.VehicleRequestDto;
import com.codigofacilito.fleet.dto.VehicleResponseDto;
import com.codigofacilito.fleet.service.VehicleService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Controlador REST para el CRUD de Vehículos y Unidades de la flota (Criterio de Evaluación #1).
 */
@RestController
@RequestMapping("/api/v1/vehicles")
@Tag(name = "Vehículos", description = "Endpoints para la gestión del parque vehicular y asignación de unidades")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar nuevo vehículo", description = "Añade una unidad de transporte a la flota")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vehículo registrado", content = @Content(schema = @Schema(implementation = VehicleResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "VIN o placa duplicados")
    })
    public Mono<ResponseEntity<VehicleResponseDto>> createVehicle(@Valid @RequestBody VehicleRequestDto requestDto) {
        return vehicleService.createVehicle(requestDto)
                .map(created -> ResponseEntity
                        .created(URI.create("/api/v1/vehicles/" + created.getId()))
                        .body(created));
    }

    @GetMapping
    @Operation(summary = "Listar vehículos", description = "Obtiene los vehículos de la flota, con filtro opcional por estado")
    @ApiResponse(responseCode = "200", description = "Listado de vehículos obtenido")
    public Flux<VehicleResponseDto> getAllVehicles(
            @Parameter(description = "Filtro opcional por estado: ACTIVE, INACTIVE, IN_TRANSIT, MAINTENANCE")
            @RequestParam(required = false) String status) {
        return vehicleService.getAllVehicles(status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener vehículo por ID", description = "Consulta los detalles de un vehículo específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    public Mono<ResponseEntity<VehicleResponseDto>> getVehicleById(@PathVariable Long id) {
        return vehicleService.getVehicleById(id)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar vehículo completo", description = "Actualiza todos los atributos de un vehículo existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehículo actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    public Mono<ResponseEntity<VehicleResponseDto>> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleRequestDto requestDto) {
        return vehicleService.updateVehicle(id, requestDto)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Actualizar estado del vehículo", description = "Modifica únicamente el estado operativo de una unidad")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado del vehículo actualizado"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    public Mono<ResponseEntity<VehicleResponseDto>> updateVehicleStatus(
            @PathVariable Long id,
            @Parameter(description = "Nuevo estado: ACTIVE, INACTIVE, IN_TRANSIT, MAINTENANCE")
            @RequestParam String status) {
        return vehicleService.updateVehicleStatus(id, status)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar vehículo", description = "Elimina un vehículo de la flota (requiere rol ROLE_ADMIN)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vehículo eliminado"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    public Mono<ResponseEntity<Void>> deleteVehicle(@PathVariable Long id) {
        return vehicleService.deleteVehicle(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
}
