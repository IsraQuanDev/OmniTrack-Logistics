package com.codigofacilito.alert.service;

import com.codigofacilito.alert.dto.AlertResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * Servicio reactivo de difusión para transmisión en vivo de alertas generadas hacia clientes suscritos (SSE).
 */
@Service
public class AlertBroadcastService {

    private static final Logger log = LoggerFactory.getLogger(AlertBroadcastService.class);

    private final Sinks.Many<AlertResponseDto> alertSink =
            Sinks.many().multicast().onBackpressureBuffer();

    public void publishAlert(AlertResponseDto alert) {
        Sinks.EmitResult result = alertSink.tryEmitNext(alert);
        if (result.isFailure()) {
            log.warn("Fallo al emitir alerta reactiva para vehículo {}: {}", alert.getVehicleId(), result);
        }
    }

    public Flux<AlertResponseDto> getAlertStream() {
        return alertSink.asFlux();
    }
}
