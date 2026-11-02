package com.codigofacilito.fleet.service;

import com.codigofacilito.fleet.dto.ShipmentRequestDto;
import com.codigofacilito.fleet.dto.ShipmentResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Contrato de servicio para la gestión de envíos y logística.
 */
public interface ShipmentService {

    Mono<ShipmentResponseDto> createShipment(ShipmentRequestDto requestDto);

    Mono<ShipmentResponseDto> getShipmentById(Long id);

    Mono<ShipmentResponseDto> getShipmentByTrackingNumber(String trackingNumber);

    Flux<ShipmentResponseDto> getAllShipments(String status);

    Mono<ShipmentResponseDto> updateShipmentStatus(Long id, String status);

    Mono<Void> deleteShipment(Long id);
}
