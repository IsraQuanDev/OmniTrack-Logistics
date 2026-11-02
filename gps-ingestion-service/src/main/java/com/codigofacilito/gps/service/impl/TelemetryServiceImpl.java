package com.codigofacilito.gps.service.impl;

import com.codigofacilito.gps.dto.BatchTelemetryRequestDto;
import com.codigofacilito.gps.dto.BatchTelemetryResponseDto;
import com.codigofacilito.gps.dto.TelemetryRequestDto;
import com.codigofacilito.gps.dto.TelemetryResponseDto;
import com.codigofacilito.gps.exception.InvalidCoordinateException;
import com.codigofacilito.gps.exception.ResourceNotFoundException;
import com.codigofacilito.gps.model.TelemetryRecord;
import com.codigofacilito.gps.repository.TelemetryRepository;
import com.codigofacilito.gps.service.TelemetryBroadcastService;
import com.codigofacilito.gps.service.TelemetryService;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la capa de servicio para la ingesta y consulta de telemetría GPS.
 */
@Service
public class TelemetryServiceImpl implements TelemetryService {

    private final TelemetryRepository telemetryRepository;
    private final TelemetryBroadcastService broadcastService;

    public TelemetryServiceImpl(TelemetryRepository telemetryRepository, TelemetryBroadcastService broadcastService) {
        this.telemetryRepository = telemetryRepository;
        this.broadcastService = broadcastService;
    }

    @Override
    public Mono<TelemetryResponseDto> ingestTelemetry(TelemetryRequestDto requestDto) {
        validateCoordinates(requestDto.getLatitude(), requestDto.getLongitude());

        TelemetryRecord record = new TelemetryRecord();
        record.setVehicleId(requestDto.getVehicleId());
        record.setDeviceId(requestDto.getDeviceId());
        record.setLatitude(requestDto.getLatitude());
        record.setLongitude(requestDto.getLongitude());
        record.setAltitude(requestDto.getAltitude());
        record.setSpeedKmh(requestDto.getSpeedKmh());
        record.setHeading(requestDto.getHeading());
        record.setBatteryLevel(requestDto.getBatteryLevel());
        record.setFuelLevel(requestDto.getFuelLevel());
        record.setTimestamp(requestDto.getTimestamp() != null ? requestDto.getTimestamp() : LocalDateTime.now());

        return telemetryRepository.save(record)
                .map(this::mapToDtoWithLinks)
                .doOnNext(broadcastService::publish);
    }

    @Override
    public Mono<BatchTelemetryResponseDto> ingestBatch(BatchTelemetryRequestDto batchRequest) {
        List<TelemetryRecord> records = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (TelemetryRequestDto dto : batchRequest.getRecords()) {
            validateCoordinates(dto.getLatitude(), dto.getLongitude());

            TelemetryRecord record = new TelemetryRecord();
            record.setVehicleId(dto.getVehicleId());
            record.setDeviceId(dto.getDeviceId());
            record.setLatitude(dto.getLatitude());
            record.setLongitude(dto.getLongitude());
            record.setAltitude(dto.getAltitude());
            record.setSpeedKmh(dto.getSpeedKmh());
            record.setHeading(dto.getHeading());
            record.setBatteryLevel(dto.getBatteryLevel());
            record.setFuelLevel(dto.getFuelLevel());
            record.setTimestamp(dto.getTimestamp() != null ? dto.getTimestamp() : now);
            records.add(record);
        }

        return telemetryRepository.saveAll(records)
                .map(this::mapToDtoWithLinks)
                .doOnNext(broadcastService::publish)
                .map(TelemetryResponseDto::getId)
                .collectList()
                .map(ids -> new BatchTelemetryResponseDto(ids.size(), "BATCH_PROCESSED_SUCCESSFULLY", ids));
    }

    @Override
    public Mono<TelemetryResponseDto> getLatestTelemetry(Long vehicleId) {
        return telemetryRepository.findLatestByVehicleId(vehicleId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("No se encontró telemetría para el vehículo con ID: " + vehicleId)))
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Flux<TelemetryResponseDto> getTelemetryHistory(Long vehicleId, LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime start = startTime != null ? startTime : LocalDateTime.now().minusDays(7);
        LocalDateTime end = endTime != null ? endTime : LocalDateTime.now();

        return telemetryRepository.findHistoryByVehicleIdAndRange(vehicleId, start, end)
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Flux<TelemetryResponseDto> streamVehicleTelemetry(Long vehicleId) {
        return broadcastService.getVehicleStream(vehicleId);
    }

    private void validateCoordinates(Double latitude, Double longitude) {
        if (latitude == null || latitude < -90.0 || latitude > 90.0) {
            throw new InvalidCoordinateException("La latitud " + latitude + " está fuera del rango permitido [-90.0, 90.0]");
        }
        if (longitude == null || longitude < -180.0 || longitude > 180.0) {
            throw new InvalidCoordinateException("La longitud " + longitude + " está fuera del rango permitido [-180.0, 180.0]");
        }
    }

    private TelemetryResponseDto mapToDtoWithLinks(TelemetryRecord record) {
        TelemetryResponseDto dto = new TelemetryResponseDto(
                record.getId(),
                record.getVehicleId(),
                record.getDeviceId(),
                record.getLatitude(),
                record.getLongitude(),
                record.getAltitude(),
                record.getSpeedKmh(),
                record.getHeading(),
                record.getBatteryLevel(),
                record.getFuelLevel(),
                record.getTimestamp()
        );
        dto.add(Link.of("/api/v1/telemetry/latest/" + record.getVehicleId()).withRel("latest-position"));
        dto.add(Link.of("/api/v1/telemetry/history/" + record.getVehicleId()).withRel("telemetry-history"));
        dto.add(Link.of("/api/v1/telemetry/stream/" + record.getVehicleId()).withRel("live-stream"));
        return dto;
    }
}
