package com.pucminas.car_rental_system.domain.dto;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para execução de contrato de aluguel — inclui dados do carro.
 */
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecuteContractDTO {

    @NotNull(message = "ID do pedido é obrigatório")
    private Long rentalRequestId;

    @Valid
    @NotNull(message = "Dados do carro são obrigatórios")
    private CarDTO car;

    @NotNull(message = "Valor total é obrigatório")
    private BigDecimal totalValue;

    /** CLIENT, COMPANY ou BANK */
    private String carOwnerType;
    private String carOwnerName;
}

