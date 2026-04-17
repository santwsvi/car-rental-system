package com.pucminas.car_rental_system.domain.dto;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de resposta para exibição de pedidos de aluguel.
 */
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalRequestResponseDTO {

    private Long id;
    private Long clientId;
    private String clientName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String notes;
    private String evaluatedBy;
    private String evaluatedByRole;
    private CarDTO car;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean canModify;
    private boolean canCancel;
}

