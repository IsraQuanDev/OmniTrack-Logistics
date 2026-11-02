package com.codigofacilito.alert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservicio Alert-Service.
 * Responsable de la monitorización reactiva de eventos críticos, detección de incidentes en tiempo real
 * (exceso de velocidad, salidas de geocerca, batería baja, botón de pánico), evaluación de telemetría y streaming reactivo SSE.
 */
@SpringBootApplication
public class AlertServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlertServiceApplication.class, args);
    }
}
