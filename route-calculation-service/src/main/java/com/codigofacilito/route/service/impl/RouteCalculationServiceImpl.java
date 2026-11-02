package com.codigofacilito.route.service.impl;

import com.codigofacilito.route.dto.EtaCalculationRequestDto;
import com.codigofacilito.route.dto.EtaCalculationResponseDto;
import com.codigofacilito.route.model.RouteCalculationRecord;
import com.codigofacilito.route.repository.RouteCalculationRepository;
import com.codigofacilito.route.service.GeoSpatialUtils;
import com.codigofacilito.route.service.RouteCalculationService;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Implementación de la lógica de negocio para el cálculo de distancia y tiempo estimado de llegada (ETA).
 */
@Service
public class RouteCalculationServiceImpl implements RouteCalculationService {

    private final RouteCalculationRepository calculationRepository;

    public RouteCalculationServiceImpl(RouteCalculationRepository calculationRepository) {
        this.calculationRepository = calculationRepository;
    }

    @Override
    public Mono<EtaCalculationResponseDto> calculateEta(EtaCalculationRequestDto requestDto) {
        double distanceKm = GeoSpatialUtils.calculateDistanceKm(
                requestDto.getCurrentLatitude(),
                requestDto.getCurrentLongitude(),
                requestDto.getDestLatitude(),
                requestDto.getDestLongitude()
        );

        double speedKmh = requestDto.getCurrentSpeedKmh() != null && requestDto.getCurrentSpeedKmh() > 0
                ? requestDto.getCurrentSpeedKmh()
                : 60.0;

        double etaMinutes = (distanceKm / speedKmh) * 60.0;
        long etaSeconds = Math.round(etaMinutes * 60.0);
        LocalDateTime arrivalTime = LocalDateTime.now().plusSeconds(etaSeconds);
        String formattedEta = formatEtaString(etaMinutes);

        RouteCalculationRecord record = new RouteCalculationRecord();
        record.setVehicleId(requestDto.getVehicleId());
        record.setOriginLatitude(requestDto.getOriginLatitude());
        record.setOriginLongitude(requestDto.getOriginLongitude());
        record.setDestLatitude(requestDto.getDestLatitude());
        record.setDestLongitude(requestDto.getDestLongitude());
        record.setCurrentLatitude(requestDto.getCurrentLatitude());
        record.setCurrentLongitude(requestDto.getCurrentLongitude());
        record.setCurrentSpeedKmh(speedKmh);
        record.setDistanceRemainingKm(Math.round(distanceKm * 100.0) / 100.0);
        record.setEtaMinutes(Math.round(etaMinutes * 10.0) / 10.0);
        record.setEstimatedArrivalTimestamp(arrivalTime);
        record.setCalculatedAt(LocalDateTime.now());

        return calculationRepository.save(record)
                .map(saved -> mapToDtoWithLinks(saved, formattedEta));
    }

    @Override
    public Flux<EtaCalculationResponseDto> getCalculationHistory(Long vehicleId) {
        return calculationRepository.findByVehicleIdOrderByCalculatedAtDesc(vehicleId)
                .map(saved -> mapToDtoWithLinks(saved, formatEtaString(saved.getEtaMinutes())));
    }

    private String formatEtaString(double minutes) {
        long totalMinutes = Math.round(minutes);
        if (totalMinutes < 60) {
            return totalMinutes + " minutos";
        }
        long hours = totalMinutes / 60;
        long remainingMinutes = totalMinutes % 60;
        return hours + " h " + remainingMinutes + " min";
    }

    private EtaCalculationResponseDto mapToDtoWithLinks(RouteCalculationRecord record, String formattedEta) {
        EtaCalculationResponseDto dto = new EtaCalculationResponseDto(
                record.getId(),
                record.getVehicleId(),
                record.getCurrentLatitude(),
                record.getCurrentLongitude(),
                record.getDestLatitude(),
                record.getDestLongitude(),
                record.getCurrentSpeedKmh(),
                record.getDistanceRemainingKm(),
                record.getEtaMinutes(),
                formattedEta,
                record.getEstimatedArrivalTimestamp(),
                record.getCalculatedAt()
        );
        dto.add(Link.of("/api/v1/routes/history/" + record.getVehicleId()).withRel("calculation-history"));
        dto.add(Link.of("/api/v1/telemetry/latest/" + record.getVehicleId()).withRel("latest-telemetry"));
        return dto;
    }
}
