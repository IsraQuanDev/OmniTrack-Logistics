package com.codigofacilito.route.service.impl;

import com.codigofacilito.route.dto.GeofenceCheckRequestDto;
import com.codigofacilito.route.dto.GeofenceCheckResponseDto;
import com.codigofacilito.route.dto.GeofenceRequestDto;
import com.codigofacilito.route.dto.GeofenceResponseDto;
import com.codigofacilito.route.exception.ResourceNotFoundException;
import com.codigofacilito.route.model.Geofence;
import com.codigofacilito.route.repository.GeofenceRepository;
import com.codigofacilito.route.service.GeoSpatialUtils;
import com.codigofacilito.route.service.GeofenceService;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Implementación de la lógica de negocio para geocercas y detección perimetral.
 */
@Service
public class GeofenceServiceImpl implements GeofenceService {

    private final GeofenceRepository geofenceRepository;

    public GeofenceServiceImpl(GeofenceRepository geofenceRepository) {
        this.geofenceRepository = geofenceRepository;
    }

    @Override
    public Mono<GeofenceResponseDto> createGeofence(GeofenceRequestDto requestDto) {
        Geofence geofence = new Geofence();
        geofence.setName(requestDto.getName());
        geofence.setDescription(requestDto.getDescription());
        geofence.setCenterLatitude(requestDto.getCenterLatitude());
        geofence.setCenterLongitude(requestDto.getCenterLongitude());
        geofence.setRadiusMeters(requestDto.getRadiusMeters());
        geofence.setActive(requestDto.getActive() != null ? requestDto.getActive() : true);
        geofence.setVehicleId(requestDto.getVehicleId());
        geofence.setCreatedAt(LocalDateTime.now());

        return geofenceRepository.save(geofence)
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<GeofenceResponseDto> getGeofenceById(Long id) {
        return geofenceRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Geocerca no encontrada con ID: " + id)))
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Flux<GeofenceResponseDto> getAllGeofences() {
        return geofenceRepository.findAll()
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<GeofenceResponseDto> updateGeofence(Long id, GeofenceRequestDto requestDto) {
        return geofenceRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Geocerca no encontrada con ID: " + id)))
                .flatMap(geofence -> {
                    geofence.setName(requestDto.getName());
                    geofence.setDescription(requestDto.getDescription());
                    geofence.setCenterLatitude(requestDto.getCenterLatitude());
                    geofence.setCenterLongitude(requestDto.getCenterLongitude());
                    geofence.setRadiusMeters(requestDto.getRadiusMeters());
                    if (requestDto.getActive() != null) {
                        geofence.setActive(requestDto.getActive());
                    }
                    geofence.setVehicleId(requestDto.getVehicleId());
                    return geofenceRepository.save(geofence);
                })
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<Void> deleteGeofence(Long id) {
        return geofenceRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Geocerca no encontrada con ID: " + id)))
                .flatMap(geofenceRepository::delete);
    }

    @Override
    public Mono<GeofenceCheckResponseDto> checkPointInGeofence(GeofenceCheckRequestDto checkRequest) {
        return geofenceRepository.findById(checkRequest.getGeofenceId())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Geocerca no encontrada con ID: " + checkRequest.getGeofenceId())))
                .map(geofence -> {
                    double distanceMeters = GeoSpatialUtils.calculateDistanceMeters(
                            geofence.getCenterLatitude(),
                            geofence.getCenterLongitude(),
                            checkRequest.getLatitude(),
                            checkRequest.getLongitude()
                    );

                    boolean isInside = distanceMeters <= geofence.getRadiusMeters();
                    String status = isInside ? "INSIDE" : "OUTSIDE";

                    return new GeofenceCheckResponseDto(
                            geofence.getId(),
                            geofence.getName(),
                            isInside,
                            status,
                            Math.round(distanceMeters * 10.0) / 10.0,
                            geofence.getRadiusMeters()
                    );
                });
    }

    private GeofenceResponseDto mapToDtoWithLinks(Geofence geofence) {
        GeofenceResponseDto dto = new GeofenceResponseDto(
                geofence.getId(),
                geofence.getName(),
                geofence.getDescription(),
                geofence.getCenterLatitude(),
                geofence.getCenterLongitude(),
                geofence.getRadiusMeters(),
                geofence.getActive(),
                geofence.getVehicleId(),
                geofence.getCreatedAt()
        );
        dto.add(Link.of("/api/v1/geofences/" + geofence.getId()).withSelfRel());
        dto.add(Link.of("/api/v1/geofences").withRel("all-geofences"));
        return dto;
    }
}
