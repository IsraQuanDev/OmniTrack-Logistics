package com.codigofacilito.gps.websocket;

import com.codigofacilito.gps.service.TelemetryBroadcastService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Handler reactivo de WebSockets para streaming bidireccional de telemetría en tiempo real.
 */
@Component
public class GpsWebSocketHandler implements WebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(GpsWebSocketHandler.class);

    private final TelemetryBroadcastService broadcastService;
    private final ObjectMapper objectMapper;

    public GpsWebSocketHandler(TelemetryBroadcastService broadcastService, ObjectMapper objectMapper) {
        this.broadcastService = broadcastService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        log.info("Nueva sesión WebSocket conectada: {}", session.getId());

        // Flujo de salida: Enviar eventos de telemetría continuos a los clientes suscritos
        Flux<WebSocketMessage> output = broadcastService.getGlobalStream()
                .map(telemetry -> {
                    try {
                        return objectMapper.writeValueAsString(telemetry);
                    } catch (Exception e) {
                        return "{}";
                    }
                })
                .map(session::textMessage);

        // Flujo de entrada: Registrar mensajes entrantes (ping/heartbeats de dispositivos)
        Mono<Void> input = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(msg -> log.debug("Mensaje WebSocket recibido en sesión {}: {}", session.getId(), msg))
                .then();

        return Mono.zip(session.send(output), input).then();
    }
}
