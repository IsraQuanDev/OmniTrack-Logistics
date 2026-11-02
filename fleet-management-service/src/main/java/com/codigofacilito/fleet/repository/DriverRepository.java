package com.codigofacilito.fleet.repository;

import com.codigofacilito.fleet.model.Driver;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo R2DBC para gestión de conductores.
 */
@Repository
public interface DriverRepository extends ReactiveCrudRepository<Driver, Long> {

    Mono<Driver> findByLicenseNumber(String licenseNumber);

    Mono<Boolean> existsByLicenseNumber(String licenseNumber);

    Mono<Boolean> existsByEmail(String email);
}
