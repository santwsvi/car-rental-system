package com.pucminas.car_rental_system.domain.dto;

import io.micronaut.core.annotation.Introspected;
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
    private String name;
    private BigDecimal income;
}
