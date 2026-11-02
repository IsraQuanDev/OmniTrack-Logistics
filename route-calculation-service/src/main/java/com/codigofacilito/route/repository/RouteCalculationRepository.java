package com.codigofacilito.route.repository;

import com.codigofacilito.route.model.RouteCalculationRecord;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Repositorio reactivo R2DBC para el historial de cálculos de ruta y ETA.
 */
@Repository
public interface RouteCalculationRepository extends ReactiveCrudRepository<RouteCalculationRecord, Long> {

    Flux<RouteCalculationRecord> findByVehicleIdOrderByCalculatedAtDesc(Long vehicleId);
}
