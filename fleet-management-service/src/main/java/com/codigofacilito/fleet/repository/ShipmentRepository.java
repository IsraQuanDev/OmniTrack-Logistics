package com.codigofacilito.fleet.repository;

import com.codigofacilito.fleet.model.Shipment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo R2DBC para gestión de envíos y despachos.
 */
@Repository
public interface ShipmentRepository extends ReactiveCrudRepository<Shipment, Long> {

    Mono<Shipment> findByTrackingNumber(String trackingNumber);

    Flux<Shipment> findByVehicleId(Long vehicleId);

    Flux<Shipment> findByStatus(String status);

    Mono<Boolean> existsByTrackingNumber(String trackingNumber);
}
