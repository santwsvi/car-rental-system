package com.pucminas.car_rental_system.domain.dto;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de resposta para exibição dos dados do cliente.
 *
 * <p>Não expõe {@code passwordHash} — proteção de PII (LGPD, Art. 46).
 * CPF é exibido mascarado em listagens, completo apenas no detalhe.</p>
 */
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String rg;
    private String cpf;
    private String profession;
    private AddressDTO address;
    private List<EmployerDTO> employers;
    private LocalDateTime createdAt;

    /**
     * Retorna o CPF mascarado para listagens (ex: ***.456.789-**).
     */
    public String getCpfMasked() {
        if (cpf == null || cpf.length() < 11) return cpf;
        String digits = cpf.replaceAll("[^0-9]", "");
        if (digits.length() < 11) return cpf;
        return "***." + digits.substring(3, 6) + "." + digits.substring(6, 9) + "-**";
    }
}
