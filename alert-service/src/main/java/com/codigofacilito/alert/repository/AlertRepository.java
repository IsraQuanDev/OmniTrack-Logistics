package com.codigofacilito.alert.repository;

import com.codigofacilito.alert.model.Alert;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Repositorio reactivo R2DBC para alertas.
 */
@Repository
public interface AlertRepository extends ReactiveCrudRepository<Alert, Long> {

    Flux<Alert> findByVehicleIdOrderByCreatedAtDesc(Long vehicleId);

    Flux<Alert> findByResolvedOrderByCreatedAtDesc(Boolean resolved);

    Flux<Alert> findBySeverityOrderByCreatedAtDesc(String severity);
}
