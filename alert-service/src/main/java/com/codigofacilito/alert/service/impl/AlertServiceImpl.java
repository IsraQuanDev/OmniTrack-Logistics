package com.codigofacilito.alert.service.impl;

import com.codigofacilito.alert.dto.AlertRequestDto;
import com.codigofacilito.alert.dto.AlertResponseDto;
import com.codigofacilito.alert.dto.TelemetryEvaluationRequestDto;
import com.codigofacilito.alert.dto.TelemetryEvaluationResponseDto;
import com.codigofacilito.alert.exception.ResourceNotFoundException;
import com.codigofacilito.alert.model.Alert;
import com.codigofacilito.alert.repository.AlertRepository;
import com.codigofacilito.alert.service.AlertBroadcastService;
import com.codigofacilito.alert.service.AlertService;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la lógica de negocio para la gestión y evaluación automática de alertas.
 */
@Service
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    private final AlertBroadcastService broadcastService;

    public AlertServiceImpl(AlertRepository alertRepository, AlertBroadcastService broadcastService) {
        this.alertRepository = alertRepository;
        this.broadcastService = broadcastService;
    }

    @Override
    public Mono<AlertResponseDto> createAlert(AlertRequestDto requestDto) {
        Alert alert = new Alert();
        alert.setVehicleId(requestDto.getVehicleId());
        alert.setAlertType(requestDto.getAlertType());
        alert.setSeverity(requestDto.getSeverity());
        alert.setMessage(requestDto.getMessage());
        alert.setLatitude(requestDto.getLatitude());
        alert.setLongitude(requestDto.getLongitude());
        alert.setCurrentValue(requestDto.getCurrentValue());
        alert.setThresholdValue(requestDto.getThresholdValue());
        alert.setResolved(false);
        alert.setCreatedAt(LocalDateTime.now());

        return alertRepository.save(alert)
                .map(this::mapToDtoWithLinks)
                .doOnNext(broadcastService::publishAlert);
    }

    @Override
    public Mono<AlertResponseDto> getAlertById(Long id) {
        return alertRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Alerta no encontrada con ID: " + id)))
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Flux<AlertResponseDto> getAlertsByVehicle(Long vehicleId) {
        return alertRepository.findByVehicleIdOrderByCreatedAtDesc(vehicleId)
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Flux<AlertResponseDto> getAllAlerts(Boolean resolved, String severity) {
        if (resolved != null) {
            return alertRepository.findByResolvedOrderByCreatedAtDesc(resolved)
                    .map(this::mapToDtoWithLinks);
        }
        if (severity != null && !severity.isBlank()) {
            return alertRepository.findBySeverityOrderByCreatedAtDesc(severity)
                    .map(this::mapToDtoWithLinks);
        }
        return alertRepository.findAll()
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<AlertResponseDto> resolveAlert(Long id) {
        return alertRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Alerta no encontrada con ID: " + id)))
                .flatMap(alert -> {
                    alert.setResolved(true);
                    alert.setResolvedAt(LocalDateTime.now());
                    return alertRepository.save(alert);
                })
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<Void> deleteAlert(Long id) {
        return alertRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Alerta no encontrada con ID: " + id)))
                .flatMap(alertRepository::delete);
    }

    @Override
    public Mono<TelemetryEvaluationResponseDto> evaluateTelemetry(TelemetryEvaluationRequestDto request) {
        List<Alert> triggered = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // 1. Evaluación de Botón de Pánico
        if (Boolean.TRUE.equals(request.getPanicButtonPressed())) {
            Alert panic = new Alert(null, request.getVehicleId(), "PANIC_BUTTON", "CRITICAL",
                    "¡BOTÓN DE PÁNICO ACTIVADO por el conductor!", request.getLatitude(), request.getLongitude(), 1.0, 0.0, false, null, now);
            triggered.add(panic);
        }

        // 2. Evaluación de Exceso de Velocidad (> 95.0 km/h)
        if (request.getSpeedKmh() != null && request.getSpeedKmh() > 95.0) {
            Alert speedAlert = new Alert(null, request.getVehicleId(), "SPEED_LIMIT_EXCEEDED", "HIGH",
                    "Velocidad excesiva detectada: " + request.getSpeedKmh() + " km/h (límite 95.0 km/h)",
                    request.getLatitude(), request.getLongitude(), request.getSpeedKmh(), 95.0, false, null, now);
            triggered.add(speedAlert);
        }

        // 3. Evaluación de Batería Baja (< 15%)
        if (request.getBatteryLevel() != null && request.getBatteryLevel() < 15.0) {
            Alert batAlert = new Alert(null, request.getVehicleId(), "LOW_BATTERY", "MEDIUM",
                    "Nivel de batería crítico en dispositivo rastreador: " + request.getBatteryLevel() + "%",
                    request.getLatitude(), request.getLongitude(), request.getBatteryLevel(), 15.0, false, null, now);
            triggered.add(batAlert);
        }

        // 4. Evaluación de Combustible Crítico (< 10%)
        if (request.getFuelLevel() != null && request.getFuelLevel() < 10.0) {
            Alert fuelAlert = new Alert(null, request.getVehicleId(), "LOW_FUEL", "HIGH",
                    "Nivel de combustible en reserva crítica: " + request.getFuelLevel() + "%",
                    request.getLatitude(), request.getLongitude(), request.getFuelLevel(), 10.0, false, null, now);
            triggered.add(fuelAlert);
        }

        if (triggered.isEmpty()) {
            return Mono.just(new TelemetryEvaluationResponseDto(request.getVehicleId(), "NORMAL", 0, List.of()));
        }

        return alertRepository.saveAll(triggered)
                .map(this::mapToDtoWithLinks)
                .doOnNext(broadcastService::publishAlert)
                .collectList()
                .map(alerts -> new TelemetryEvaluationResponseDto(
                        request.getVehicleId(),
                        "ANOMALY_DETECTED",
                        alerts.size(),
                        alerts
                ));
    }

    @Override
    public Flux<AlertResponseDto> streamAlerts() {
        return broadcastService.getAlertStream();
    }

    private AlertResponseDto mapToDtoWithLinks(Alert alert) {
        AlertResponseDto dto = new AlertResponseDto(
                alert.getId(),
                alert.getVehicleId(),
                alert.getAlertType(),
                alert.getSeverity(),
                alert.getMessage(),
                alert.getLatitude(),
                alert.getLongitude(),
                alert.getCurrentValue(),
                alert.getThresholdValue(),
                alert.getResolved(),
                alert.getResolvedAt(),
                alert.getCreatedAt()
        );
        dto.add(Link.of("/api/v1/alerts/" + alert.getId()).withSelfRel());
        dto.add(Link.of("/api/v1/alerts/vehicle/" + alert.getVehicleId()).withRel("vehicle-alerts"));
        dto.add(Link.of("/api/v1/alerts/" + alert.getId() + "/resolve").withRel("resolve-alert"));
        return dto;
    }
}
