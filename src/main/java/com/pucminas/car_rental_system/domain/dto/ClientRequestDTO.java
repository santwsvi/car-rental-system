package com.pucminas.car_rental_system.domain.dto;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO de entrada para criação e edição de um cliente.
 *
 * <p>Contém apenas dados que o formulário web submete.
 * Nunca expõe {@code passwordHash} ou identificadores internos
 * de forma direta — separação DTO vs Entity (Clean Architecture).</p>
 */
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255)
    private String name;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres")
    private String password;

    @NotBlank(message = "RG é obrigatório")
    @Size(max = 20)
    private String rg;

    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 14, message = "CPF deve ter entre 11 e 14 caracteres")
    private String cpf;

    @Size(max = 100)
    private String profession;

    @Valid
    @Builder.Default
    private AddressDTO address = new AddressDTO();

    @Size(max = 3, message = "Máximo de 3 empregadores")
    @Builder.Default
    private List<EmployerDTO> employers = new ArrayList<>(List.of(
            new EmployerDTO(), new EmployerDTO(), new EmployerDTO()
    ));

    /**
     * Garante que a lista de empregadores sempre tenha 3 slots
     * para o binding correto do Thymeleaf (employers[0], [1], [2]).
     */
    public void ensureEmployerSlots() {
        if (this.employers == null) {
            this.employers = new ArrayList<>();
        }
        while (this.employers.size() < 3) {
            this.employers.add(new EmployerDTO());
        }
    }
}

