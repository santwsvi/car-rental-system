package com.pucminas.car_rental_system.domain.entity;

import io.micronaut.core.annotation.Introspected;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Value Object de endereço — imutabilidade semântica (DDD, Evans).
 *
 * <p>Modelado como {@link Embeddable} JPA: armazenado inline na tabela
 * da entidade proprietária, sem tabela própria nem ciclo de vida
 * independente — composição UML (diamante preenchido).</p>
 */
@Embeddable
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @NotBlank(message = "Rua é obrigatória")
    @Size(max = 255)
    @Column(name = "address_street")
    private String street;

    @NotBlank(message = "Número é obrigatório")
    @Size(max = 20)
    @Column(name = "address_number")
    private String number;

    @Size(max = 100)
    @Column(name = "address_complement")
    private String complement;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 100)
    @Column(name = "address_city")
    private String city;

    @NotBlank(message = "Estado é obrigatório")
    @Size(max = 50)
    @Column(name = "address_state")
    private String state;

    @NotBlank(message = "CEP é obrigatório")
    @Size(max = 10)
    @Column(name = "address_zip_code")
    private String zipCode;

    @Size(max = 50)
    @Column(name = "address_country")
    @Builder.Default
    private String country = "Brasil";
}



