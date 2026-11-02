package com.codigofacilito.route.exception;

/**
 * Excepción lanzada cuando una geocerca o cálculo de ruta no existe.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
