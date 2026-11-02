package com.codigofacilito.gps.exception;

/**
 * Excepción lanzada cuando los datos de telemetría contienen coordenadas geográficas no válidas.
 */
public class InvalidCoordinateException extends RuntimeException {

    public InvalidCoordinateException(String message) {
        super(message);
    }
}
