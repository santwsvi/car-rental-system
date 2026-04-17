package com.pucminas.car_rental_system.domain.enums;

/**
 * Máquina de estados do pedido de aluguel — GoF State Pattern (implícito).
 *
 * <p>Transições válidas:</p>
 * <pre>
 *   PENDING → UNDER_REVIEW → APPROVED → CONTRACTED
 *                           → REJECTED
 *   PENDING → CANCELLED
 *   UNDER_REVIEW → CANCELLED
 * </pre>
 *
 * @see com.pucminas.car_rental_system.domain.entity.RentalRequest
 */
public enum RequestStatus {

    PENDING,
    UNDER_REVIEW,
    APPROVED,
    REJECTED,
    CONTRACTED,
    CANCELLED
}

