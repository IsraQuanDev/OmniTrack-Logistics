package com.codigofacilito.route.service;

import com.codigofacilito.route.dto.GeofenceCheckRequestDto;
import com.codigofacilito.route.dto.GeofenceCheckResponseDto;
import com.codigofacilito.route.dto.GeofenceRequestDto;
import com.codigofacilito.route.dto.GeofenceResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Contrato de servicio para la gestión y validación perimetral de geocercas.
 */
public interface GeofenceService {

    Mono<GeofenceResponseDto> createGeofence(GeofenceRequestDto requestDto);

    Mono<GeofenceResponseDto> getGeofenceById(Long id);

    Flux<GeofenceResponseDto> getAllGeofences();

    Mono<GeofenceResponseDto> updateGeofence(Long id, GeofenceRequestDto requestDto);

    Mono<Void> deleteGeofence(Long id);

    Mono<GeofenceCheckResponseDto> checkPointInGeofence(GeofenceCheckRequestDto checkRequest);
}
