package com.pucminas.car_rental_system.exception;

/**
 * Exceção lançada quando uma regra de negócio é violada.
 * Tratada pelo {@link GlobalExceptionHandler} → HTTP 422.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
