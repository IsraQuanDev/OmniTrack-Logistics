package com.codigofacilito.route;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservicio Route-Calculation-Service.
 * Responsable del cálculo geoespacial de distancias, cálculo dinámico de tiempo estimado de llegada (ETA)
 * y supervisión de geocercas (Geofencing) en arquitectura reactiva no bloqueante con WebFlux.
 */
@SpringBootApplication
public class RouteCalculationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RouteCalculationServiceApplication.class, args);
    }
}
