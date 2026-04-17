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

/**
 * DTO para dados do automóvel na execução do contrato.
 */
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarDTO {

    private Long id;

    @NotBlank(message = "Matrícula é obrigatória")
    @Size(max = 50)
    private String registration;

    @NotNull(message = "Ano é obrigatório")
    private Integer year;

    @NotBlank(message = "Marca é obrigatória")
    @Size(max = 100)
    private String brand;

    @NotBlank(message = "Modelo é obrigatório")
    @Size(max = 100)
    private String model;

    @NotBlank(message = "Placa é obrigatória")
    @Size(max = 10)
    private String plate;
}

