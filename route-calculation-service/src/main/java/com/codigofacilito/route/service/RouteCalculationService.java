package com.codigofacilito.route.service;

import com.codigofacilito.route.dto.EtaCalculationRequestDto;
import com.codigofacilito.route.dto.EtaCalculationResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Contrato de servicio para cálculo geoespacial de rutas y estimación de tiempo de arribo (ETA).
 */
public interface RouteCalculationService {

    Mono<EtaCalculationResponseDto> calculateEta(EtaCalculationRequestDto requestDto);

    Flux<EtaCalculationResponseDto> getCalculationHistory(Long vehicleId);
}
