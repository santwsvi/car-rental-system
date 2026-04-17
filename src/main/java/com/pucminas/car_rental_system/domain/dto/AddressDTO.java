package com.pucminas.car_rental_system.domain.dto;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para endereço embutido nos formulários de cliente.
 */
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {

    @NotBlank(message = "Rua é obrigatória")
    @Size(max = 255)
    private String street;

    @NotBlank(message = "Número é obrigatório")
    @Size(max = 20)
    private String number;

    @Size(max = 100)
    private String complement;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 100)
    private String city;

    @NotBlank(message = "Estado é obrigatório")
    @Size(max = 50)
    private String state;

    @NotBlank(message = "CEP é obrigatório")
    @Size(max = 10)
    private String zipCode;

    @Size(max = 50)
    @Builder.Default
    private String country = "Brasil";
}
