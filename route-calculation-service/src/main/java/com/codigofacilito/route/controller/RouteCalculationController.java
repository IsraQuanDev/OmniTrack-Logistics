package com.codigofacilito.route.controller;

import com.codigofacilito.route.dto.EtaCalculationRequestDto;
import com.codigofacilito.route.dto.EtaCalculationResponseDto;
import com.codigofacilito.route.service.RouteCalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador REST para el cálculo de distancias y tiempo estimado de llegada (ETA).
 */
@RestController
@RequestMapping("/api/v1/routes")
@Tag(name = "Cálculo de Rutas y ETA", description = "Endpoints para estimación de tiempos de llegada y distancia de viaje")
public class RouteCalculationController {

    private final RouteCalculationService routeCalculationService;

    public RouteCalculationController(RouteCalculationService routeCalculationService) {
        this.routeCalculationService = routeCalculationService;
    }

    @PostMapping("/calculate-eta")
    @Operation(summary = "Calcular tiempo estimado de llegada (ETA)", description = "Calcula la distancia remanente y el ETA en base a la posición GPS actual y velocidad del vehículo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cálculo de ETA generado exitosamente", content = @Content(schema = @Schema(implementation = EtaCalculationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Coordenadas o velocidad inválidas")
    })
    public Mono<ResponseEntity<EtaCalculationResponseDto>> calculateEta(@Valid @RequestBody EtaCalculationRequestDto requestDto) {
        return routeCalculationService.calculateEta(requestDto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/history/{vehicleId}")
    @Operation(summary = "Consultar historial de cálculos ETA", description = "Obtiene los registros de cálculo de ruta y ETA calculados para un vehículo")
    @ApiResponse(responseCode = "200", description = "Historial obtenido")
    public Flux<EtaCalculationResponseDto> getHistory(@PathVariable Long vehicleId) {
        return routeCalculationService.getCalculationHistory(vehicleId);
    }
}
