package com.codigofacilito.fleet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservicio Fleet-Management-Service.
 * Responsable de la gestión de flotas, vehículos, conductores, envíos y usuarios del sistema.
 * Basado en arquitectura reactiva no bloqueante con Spring WebFlux y R2DBC.
 */
@SpringBootApplication
public class FleetManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FleetManagementServiceApplication.class, args);
    }
}
