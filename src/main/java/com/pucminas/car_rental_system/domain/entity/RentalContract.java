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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade RentalContract — contrato de aluguel executado.
 */
@Entity
@Table(name = "rental_contracts")
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_request_id", nullable = false, unique = true)
    @NotNull
    private RentalRequest rentalRequest;

    @Column(name = "start_date", nullable = false)
    @NotNull
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @NotNull
    private LocalDate endDate;

    @Column(name = "total_value", nullable = false, precision = 15, scale = 2)
    @NotNull
    private BigDecimal totalValue;

    /** Proprietário do automóvel: CLIENT, COMPANY ou BANK */
    @Column(name = "car_owner_type")
    private String carOwnerType;

    @Column(name = "car_owner_name")
    private String carOwnerName;

    @Column(name = "signed_at", nullable = false, updatable = false)
    private LocalDateTime signedAt;

    @PrePersist
    protected void onCreate() {
        this.signedAt = LocalDateTime.now();
    }
}

