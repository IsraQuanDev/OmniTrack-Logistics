package com.codigofacilito.fleet.service;

import com.codigofacilito.fleet.dto.DriverRequestDto;
import com.codigofacilito.fleet.dto.DriverResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Contrato de servicio para la administración de conductores de la flota.
 */
public interface DriverService {

    Mono<DriverResponseDto> createDriver(DriverRequestDto requestDto);

    Mono<DriverResponseDto> getDriverById(Long id);

    Flux<DriverResponseDto> getAllDrivers();

    Mono<DriverResponseDto> updateDriver(Long id, DriverRequestDto requestDto);

    Mono<Void> deleteDriver(Long id);
}
