package com.codigofacilito.gps.service;

import com.codigofacilito.gps.dto.TelemetryResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * Servicio reactivo de difusión (Pub/Sub) para streaming de telemetría en tiempo real.
 * Utiliza Project Reactor Sinks para distribuir coordenadas hacia WebSockets y SSE.
 */
@Service
public class TelemetryBroadcastService {

    private static final Logger log = LoggerFactory.getLogger(TelemetryBroadcastService.class);

    private final Sinks.Many<TelemetryResponseDto> telemetrySink =
            Sinks.many().multicast().onBackpressureBuffer();

    public void publish(TelemetryResponseDto telemetry) {
        Sinks.EmitResult result = telemetrySink.tryEmitNext(telemetry);
        if (result.isFailure()) {
            log.warn("Fallo al emitir telemetría reactiva para el vehículo {}: {}", telemetry.getVehicleId(), result);
        }
    }

    public Flux<TelemetryResponseDto> getGlobalStream() {
        return telemetrySink.asFlux();
    }

    public Flux<TelemetryResponseDto> getVehicleStream(Long vehicleId) {
        return telemetrySink.asFlux()
                .filter(dto -> dto.getVehicleId().equals(vehicleId));
    }
}
