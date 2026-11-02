package com.codigofacilito.fleet.controller;

import com.codigofacilito.fleet.dto.DriverRequestDto;
import com.codigofacilito.fleet.dto.DriverResponseDto;
import com.codigofacilito.fleet.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Controlador REST para el CRUD de Conductores (Drivers).
 */
@RestController
@RequestMapping("/api/v1/drivers")
@Tag(name = "Conductores", description = "Endpoints para la gestión y asignación de transportistas y conductores")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar nuevo conductor", description = "Registra un nuevo operador de transporte en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conductor registrado", content = @Content(schema = @Schema(implementation = DriverResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Número de licencia o correo ya existente")
    })
    public Mono<ResponseEntity<DriverResponseDto>> createDriver(@Valid @RequestBody DriverRequestDto requestDto) {
        return driverService.createDriver(requestDto)
                .map(created -> ResponseEntity
                        .created(URI.create("/api/v1/drivers/" + created.getId()))
                        .body(created));
    }

    @GetMapping
    @Operation(summary = "Listar conductores", description = "Obtiene el listado de todos los conductores registrados")
    @ApiResponse(responseCode = "200", description = "Lista de conductores obtenida")
    public Flux<DriverResponseDto> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener conductor por ID", description = "Consulta la información detallada de un conductor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conductor encontrado"),
            @ApiResponse(responseCode = "404", description = "Conductor no encontrado")
    })
    public Mono<ResponseEntity<DriverResponseDto>> getDriverById(@PathVariable Long id) {
        return driverService.getDriverById(id)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar conductor", description = "Modifica los datos de contacto o estado de un conductor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conductor actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Conductor no encontrado")
    })
    public Mono<ResponseEntity<DriverResponseDto>> updateDriver(@PathVariable Long id, @Valid @RequestBody DriverRequestDto requestDto) {
        return driverService.updateDriver(id, requestDto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar conductor", description = "Elimina un registro de conductor (requiere rol ROLE_ADMIN)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Conductor eliminado"),
            @ApiResponse(responseCode = "404", description = "Conductor no encontrado")
    })
    public Mono<ResponseEntity<Void>> deleteDriver(@PathVariable Long id) {
        return driverService.deleteDriver(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
}
