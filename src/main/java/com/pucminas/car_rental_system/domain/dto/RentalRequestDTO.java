package com.pucminas.car_rental_system.domain.dto;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO de entrada para criação/edição de pedido de aluguel.
 */
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalRequestDTO {

    private Long clientId;

    @NotNull(message = "Data de início é obrigatória")
    private LocalDate startDate;

    @NotNull(message = "Data de término é obrigatória")
    private LocalDate endDate;

    @Size(max = 1000)
    private String notes;
}

