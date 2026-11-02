package com.codigofacilito.alert.exception;

/**
 * Excepción lanzada cuando una alerta o regla solicitada no existe.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
