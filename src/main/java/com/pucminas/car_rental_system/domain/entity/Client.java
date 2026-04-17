package com.pucminas.car_rental_system.domain.entity;

import com.pucminas.car_rental_system.domain.enums.UserRole;
import io.micronaut.core.annotation.Introspected;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade Cliente — pessoa física que contrata aluguel de automóveis.
 *
 * <p>Invariantes de negócio:</p>
 * <ul>
 *   <li>CPF e RG são obrigatórios e únicos no sistema.</li>
 *   <li>Máximo de <b>3 empregadores</b> por cliente — validado na camada Service.</li>
 *   <li>Endereço é obrigatório (composição UML — ciclo de vida dependente).</li>
 * </ul>
 *
 * @see User
 * @see Employer
 * @see Address
 */
@Entity
@Table(name = "clients")
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client extends User implements CarOwner {

    public static final int MAX_EMPLOYERS = 3;

    @NotBlank(message = "RG é obrigatório")
    @Size(max = 20)
    @Column(nullable = false)
    private String rg;

    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 14)
    @Column(nullable = false, unique = true)
    private String cpf;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255)
    @Column(nullable = false)
    private String name;

    @Size(max = 100)
    private String profession;

    @Embedded
    @Valid
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Employer> employers = new ArrayList<>();

    @Override
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (getRole() == null) {
            setRole(UserRole.CLIENT);
        }
    }

    /**
     * Adiciona um empregador ao cliente respeitando o limite máximo de 3.
     *
     * @param employer empregador a ser adicionado
     * @throws IllegalStateException se o cliente já possui 3 empregadores
     */
    public void addEmployer(Employer employer) {
        if (this.employers.size() >= MAX_EMPLOYERS) {
            throw new IllegalStateException(
                    "Cliente não pode possuir mais de " + MAX_EMPLOYERS + " empregadores.");
        }
        employer.setClient(this);
        this.employers.add(employer);
    }

    /**
     * Remove um empregador do cliente.
     *
     * @param employer empregador a ser removido
     */
    public void removeEmployer(Employer employer) {
        this.employers.remove(employer);
        employer.setClient(null);
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }

    @Builder
    public Client(Long id, String email, String passwordHash, UserRole role,
                  String rg, String cpf, String name, String profession,
                  Address address, List<Employer> employers) {
        setId(id);
        setEmail(email);
        setPasswordHash(passwordHash);
        setRole(role != null ? role : UserRole.CLIENT);
        this.rg = rg;
        this.cpf = cpf;
        this.name = name;
        this.profession = profession;
        this.address = address;
        this.employers = employers != null ? employers : new ArrayList<>();
    }
}





