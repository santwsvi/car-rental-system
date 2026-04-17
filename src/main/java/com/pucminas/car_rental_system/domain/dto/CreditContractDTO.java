package com.pucminas.car_rental_system.domain.dto;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO para concessão de contrato de crédito por um banco.
 */
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditContractDTO {

    @NotNull(message = "ID do pedido é obrigatório")
    private Long rentalRequestId;

    private String bankName;

    @NotNull(message = "Valor do crédito é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor do crédito deve ser positivo")
    private BigDecimal creditValue;

    @NotNull(message = "Taxa de juros é obrigatória")
    @DecimalMin(value = "0.01", message = "Taxa de juros deve ser positiva")
    private BigDecimal interestRate;
}

