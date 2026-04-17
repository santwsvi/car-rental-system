package com.pucminas.car_rental_system.domain.entity;

import io.micronaut.core.annotation.Introspected;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade CreditContract — contrato de crédito concedido por um banco.
 */
@Entity
@Table(name = "credit_contracts")
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_request_id", nullable = false, unique = true)
    @NotNull
    private RentalRequest rentalRequest;

    @Column(name = "bank_name", nullable = false)
    @NotNull
    private String bankName;

    @Column(name = "credit_value", nullable = false, precision = 15, scale = 2)
    @NotNull
    private BigDecimal creditValue;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal interestRate;

    @Column(name = "issued_at", nullable = false, updatable = false)
    private LocalDateTime issuedAt;

    @PrePersist
    protected void onCreate() {
        this.issuedAt = LocalDateTime.now();
    }
}

