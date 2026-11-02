package com.codigofacilito.gps.service;

import com.codigofacilito.gps.dto.BatchTelemetryRequestDto;
import com.codigofacilito.gps.dto.BatchTelemetryResponseDto;
import com.codigofacilito.gps.dto.TelemetryRequestDto;
import com.codigofacilito.gps.dto.TelemetryResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Contrato de servicio para la ingesta y consulta de coordenadas GPS.
 */
public interface TelemetryService {

    Mono<TelemetryResponseDto> ingestTelemetry(TelemetryRequestDto requestDto);

    Mono<BatchTelemetryResponseDto> ingestBatch(BatchTelemetryRequestDto batchRequest);

    Mono<TelemetryResponseDto> getLatestTelemetry(Long vehicleId);

    Flux<TelemetryResponseDto> getTelemetryHistory(Long vehicleId, LocalDateTime startTime, LocalDateTime endTime);

    Flux<TelemetryResponseDto> streamVehicleTelemetry(Long vehicleId);
}
