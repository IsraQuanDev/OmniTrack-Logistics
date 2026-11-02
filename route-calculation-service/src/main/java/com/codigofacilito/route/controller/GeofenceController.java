package com.codigofacilito.route.controller;

import com.codigofacilito.route.dto.GeofenceCheckRequestDto;
import com.codigofacilito.route.dto.GeofenceCheckResponseDto;
import com.codigofacilito.route.dto.GeofenceRequestDto;
import com.codigofacilito.route.dto.GeofenceResponseDto;
import com.codigofacilito.route.service.GeofenceService;
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
 * Controlador REST para el CRUD de Geocercas y verificación perimetral.
 */
@RestController
@RequestMapping("/api/v1/geofences")
@Tag(name = "Geocercas (Geofencing)", description = "Endpoints para la creación, consulta y verificación perimetral de geocercas")
public class GeofenceController {

    private final GeofenceService geofenceService;

    public GeofenceController(GeofenceService geofenceService) {
        this.geofenceService = geofenceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear nueva geocerca", description = "Registra una zona perimetral circular con coordenadas de centro y radio en metros")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Geocerca creada exitosamente", content = @Content(schema = @Schema(implementation = GeofenceResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public Mono<ResponseEntity<GeofenceResponseDto>> createGeofence(@Valid @RequestBody GeofenceRequestDto requestDto) {
        return geofenceService.createGeofence(requestDto)
                .map(created -> ResponseEntity
                        .created(URI.create("/api/v1/geofences/" + created.getId()))
                        .body(created));
    }

    @GetMapping
    @Operation(summary = "Listar todas las geocercas", description = "Obtiene todas las geocercas registradas en el sistema")
    @ApiResponse(responseCode = "200", description = "Listado de geocercas obtenido")
    public Flux<GeofenceResponseDto> getAllGeofences() {
        return geofenceService.getAllGeofences();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener geocerca por ID", description = "Consulta los detalles y radio de una geocerca")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Geocerca encontrada"),
            @ApiResponse(responseCode = "404", description = "Geocerca no encontrada")
    })
    public Mono<ResponseEntity<GeofenceResponseDto>> getGeofenceById(@PathVariable Long id) {
        return geofenceService.getGeofenceById(id)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar geocerca", description = "Modifica el radio, centro o estado de una geocerca")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Geocerca actualizada"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Geocerca no encontrada")
    })
    public Mono<ResponseEntity<GeofenceResponseDto>> updateGeofence(@PathVariable Long id, @Valid @RequestBody GeofenceRequestDto requestDto) {
        return geofenceService.updateGeofence(id, requestDto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar geocerca", description = "Elimina una geocerca del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Geocerca eliminada"),
            @ApiResponse(responseCode = "404", description = "Geocerca no encontrada")
    })
    public Mono<ResponseEntity<Void>> deleteGeofence(@PathVariable Long id) {
        return geofenceService.deleteGeofence(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    @PostMapping("/check")
    @Operation(summary = "Verificar coordenada en geocerca", description = "Determina si una coordenada GPS se encuentra INSIDE u OUTSIDE del radio perimetral")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evaluación perimetral completada", content = @Content(schema = @Schema(implementation = GeofenceCheckResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Coordenadas inválidas"),
            @ApiResponse(responseCode = "404", description = "Geocerca no encontrada")
    })
    public Mono<ResponseEntity<GeofenceCheckResponseDto>> checkPoint(@Valid @RequestBody GeofenceCheckRequestDto checkRequest) {
        return geofenceService.checkPointInGeofence(checkRequest)
                .map(ResponseEntity::ok);
    }
}
