package com.pucminas.car_rental_system.domain.entity;

import io.micronaut.core.annotation.Introspected;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidade Car — automóvel vinculado a um contrato de aluguel.
 */
@Entity
@Table(name = "cars")
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Matrícula é obrigatória")
    @Size(max = 50)
    @Column(nullable = false, unique = true)
    private String registration;

    @NotNull(message = "Ano é obrigatório")
    @Column(nullable = false)
    private Integer year;

    @NotBlank(message = "Marca é obrigatória")
    @Size(max = 100)
    @Column(nullable = false)
    private String brand;

    @NotBlank(message = "Modelo é obrigatório")
    @Size(max = 100)
    @Column(nullable = false)
    private String model;

    @NotBlank(message = "Placa é obrigatória")
    @Size(max = 10)
    @Column(nullable = false, unique = true)
    private String plate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

