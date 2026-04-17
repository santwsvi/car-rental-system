package com.pucminas.car_rental_system.domain.entity;

import io.micronaut.core.annotation.Introspected;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entidade empregadora do cliente — relacionamento de composição.
 *
 * <p>Cada cliente pode possuir no máximo <b>3 empregadores</b>
 * (invariante de negócio validada na camada Service).</p>
 *
 * <p>O ciclo de vida do {@code Employer} é dependente do {@code Client}:
 * ao remover o cliente, todos os empregadores são removidos (cascade).</p>
 */
@Entity
@Table(name = "employers")
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do empregador é obrigatório")
    @Size(max = 255)
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Rendimento é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Rendimento deve ser positivo")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal income;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
}



