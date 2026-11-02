package com.codigofacilito.gps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservicio GPS-Ingestion-Service.
 * Responsable de la ingesta masiva de coordenadas GPS en tiempo real, validación geoespacial,
 * almacenamiento histórico y difusión reactiva mediante Server-Sent Events (SSE) y WebSockets.
 */
@SpringBootApplication
public class GpsIngestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GpsIngestionServiceApplication.class, args);
    }
}
