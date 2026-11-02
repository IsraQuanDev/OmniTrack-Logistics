package com.codigofacilito.route.repository;

import com.codigofacilito.route.model.Geofence;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Repositorio reactivo R2DBC para geocercas.
 */
@Repository
public interface GeofenceRepository extends ReactiveCrudRepository<Geofence, Long> {

    Flux<Geofence> findByVehicleId(Long vehicleId);

    Flux<Geofence> findByActive(Boolean active);
}
