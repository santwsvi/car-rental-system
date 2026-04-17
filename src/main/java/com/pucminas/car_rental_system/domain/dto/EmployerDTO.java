package com.pucminas.car_rental_system.domain.dto;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO para criação e edição de empregadores de um cliente.
 * Campos são opcionais — validação de preenchimento é feita na camada Service.
 */
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployerDTO {
    private Long id;

    @NotBlank(message = "Nome do empregador é obrigatório")
    private String name;

    @NotNull(message = "Rendimento é obrigatório")
    @DecimalMin(value = "0.01", message = "Rendimento deve ser positivo")
    private BigDecimal income;
}
