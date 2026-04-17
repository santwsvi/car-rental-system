package com.pucminas.car_rental_system.domain.entity;

import com.pucminas.car_rental_system.domain.enums.UserRole;
import io.micronaut.core.annotation.Introspected;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade Company — agente empresa que avalia pedidos de aluguel.
 */
@Entity
@Table(name = "companies")
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company extends User implements CarOwner {

    @NotBlank(message = "CNPJ é obrigatório")
    @Size(max = 18)
    @Column(nullable = false, unique = true)
    private String cnpj;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255)
    @Column(nullable = false)
    private String name;

    @Embedded
    @Valid
    private Address address;

    @Override
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (getRole() == null) {
            setRole(UserRole.AGENT_COMPANY);
        }
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }

    @Builder
    public Company(Long id, String email, String passwordHash, UserRole role,
                   String cnpj, String name, Address address) {
        setId(id);
        setEmail(email);
        setPasswordHash(passwordHash);
        setRole(role != null ? role : UserRole.AGENT_COMPANY);
        this.cnpj = cnpj;
        this.name = name;
        this.address = address;
    }
}

