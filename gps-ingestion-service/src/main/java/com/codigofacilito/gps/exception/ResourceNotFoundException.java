package com.codigofacilito.gps.exception;

/**
 * Excepción lanzada cuando una lectura o vehículo no tiene registros de telemetría.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
