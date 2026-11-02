package com.codigofacilito.fleet.service;

import com.codigofacilito.fleet.dto.VehicleRequestDto;
import com.codigofacilito.fleet.dto.VehicleResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Contrato de servicio para la gestión de vehículos y unidades de transporte.
 */
public interface VehicleService {

    Mono<VehicleResponseDto> createVehicle(VehicleRequestDto requestDto);

    Mono<VehicleResponseDto> getVehicleById(Long id);

    Flux<VehicleResponseDto> getAllVehicles(String status);

    Mono<VehicleResponseDto> updateVehicle(Long id, VehicleRequestDto requestDto);

    Mono<VehicleResponseDto> updateVehicleStatus(Long id, String status);

    Mono<Void> deleteVehicle(Long id);
}
