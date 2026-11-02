package com.codigofacilito.alert.service;

import com.codigofacilito.alert.dto.AlertRequestDto;
import com.codigofacilito.alert.dto.AlertResponseDto;
import com.codigofacilito.alert.dto.TelemetryEvaluationRequestDto;
import com.codigofacilito.alert.dto.TelemetryEvaluationResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Contrato de servicio para la gestión, resolución y evaluación reactiva de alertas.
 */
public interface AlertService {

    Mono<AlertResponseDto> createAlert(AlertRequestDto requestDto);

    Mono<AlertResponseDto> getAlertById(Long id);

    Flux<AlertResponseDto> getAlertsByVehicle(Long vehicleId);

    Flux<AlertResponseDto> getAllAlerts(Boolean resolved, String severity);

    Mono<AlertResponseDto> resolveAlert(Long id);

    Mono<Void> deleteAlert(Long id);

    Mono<TelemetryEvaluationResponseDto> evaluateTelemetry(TelemetryEvaluationRequestDto evaluationRequest);

    Flux<AlertResponseDto> streamAlerts();
}
