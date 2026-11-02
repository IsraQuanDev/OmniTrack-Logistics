package com.codigofacilito.fleet.exception;

/**
 * Excepción lanzada cuando se intenta registrar un recurso que ya existe (ej. placa, licencia o usuario duplicado).
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
