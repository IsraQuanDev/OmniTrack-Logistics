package com.codigofacilito.gps.repository;

import com.codigofacilito.gps.model.TelemetryRecord;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Repositorio reactivo R2DBC para telemetría GPS de unidades de transporte.
 */
@Repository
public interface TelemetryRepository extends ReactiveCrudRepository<TelemetryRecord, Long> {

    @Query("SELECT * FROM telemetry_records WHERE vehicle_id = :vehicleId ORDER BY timestamp DESC LIMIT 1")
    Mono<TelemetryRecord> findLatestByVehicleId(Long vehicleId);

    @Query("SELECT * FROM telemetry_records WHERE vehicle_id = :vehicleId AND timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp ASC")
    Flux<TelemetryRecord> findHistoryByVehicleIdAndRange(Long vehicleId, LocalDateTime startTime, LocalDateTime endTime);

    Flux<TelemetryRecord> findByVehicleIdOrderByTimestampDesc(Long vehicleId);
}
