package com.codigofacilito.fleet.repository;

import com.codigofacilito.fleet.model.Vehicle;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo R2DBC para gestión de vehículos.
 */
@Repository
public interface VehicleRepository extends ReactiveCrudRepository<Vehicle, Long> {

    Mono<Vehicle> findByLicensePlate(String licensePlate);

    Mono<Vehicle> findByVin(String vin);

    Flux<Vehicle> findByStatus(String status);

    Mono<Boolean> existsByLicensePlate(String licensePlate);

    Mono<Boolean> existsByVin(String vin);
}
