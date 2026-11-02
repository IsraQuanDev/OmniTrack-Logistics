package com.codigofacilito.alert.repository;

import com.codigofacilito.alert.model.AlertRule;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo R2DBC para reglas de detección de alertas.
 */
@Repository
public interface AlertRuleRepository extends ReactiveCrudRepository<AlertRule, Long> {

    Flux<AlertRule> findByActive(Boolean active);

    Mono<AlertRule> findByRuleTypeAndActive(String ruleType, Boolean active);
}
