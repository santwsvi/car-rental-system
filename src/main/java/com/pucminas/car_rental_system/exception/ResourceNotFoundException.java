package com.pucminas.car_rental_system.exception;

/**
 * Exceção lançada quando um recurso solicitado não é encontrado.
 * Tratada pelo {@link GlobalExceptionHandler} → HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " não encontrado(a) com ID: " + id);
    }
}
