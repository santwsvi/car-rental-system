package com.pucminas.car_rental_system.domain.entity;

import com.pucminas.car_rental_system.domain.enums.RequestStatus;
import io.micronaut.core.annotation.Introspected;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade RentalRequest — pedido de aluguel de automóvel.
 *
 * <p>Implementa máquina de estados via {@link RequestStatus}.</p>
 * <p>Transições válidas:</p>
 * <pre>
 *   PENDING → UNDER_REVIEW → APPROVED → CONTRACTED
 *                           → REJECTED
 *   PENDING / UNDER_REVIEW → CANCELLED
 * </pre>
 */
@Entity
@Table(name = "rental_requests")
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull
    private Client client;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "start_date", nullable = false)
    @NotNull(message = "Data de início é obrigatória")
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @NotNull(message = "Data de término é obrigatória")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RequestStatus status = RequestStatus.PENDING;

    @Column(length = 1000)
    private String notes;

    /** Nome do agente que avaliou (armazenado para auditoria). */
    @Column(name = "evaluated_by")
    private String evaluatedBy;

    /** Tipo do agente que avaliou. */
    @Enumerated(EnumType.STRING)
    @Column(name = "evaluated_by_role")
    private com.pucminas.car_rental_system.domain.enums.UserRole evaluatedByRole;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ─── State Machine Methods ─────

    public void submitForReview() {
        validateTransition(RequestStatus.UNDER_REVIEW);
        this.status = RequestStatus.UNDER_REVIEW;
    }

    public void approve(String agentName, com.pucminas.car_rental_system.domain.enums.UserRole agentRole) {
        validateTransition(RequestStatus.APPROVED);
        this.status = RequestStatus.APPROVED;
        this.evaluatedBy = agentName;
        this.evaluatedByRole = agentRole;
    }

    public void reject(String agentName, com.pucminas.car_rental_system.domain.enums.UserRole agentRole) {
        validateTransition(RequestStatus.REJECTED);
        this.status = RequestStatus.REJECTED;
        this.evaluatedBy = agentName;
        this.evaluatedByRole = agentRole;
    }

    public void cancel() {
        validateTransition(RequestStatus.CANCELLED);
        this.status = RequestStatus.CANCELLED;
    }

    public void markContracted() {
        validateTransition(RequestStatus.CONTRACTED);
        this.status = RequestStatus.CONTRACTED;
    }

    public boolean canBeModified() {
        return this.status != RequestStatus.CONTRACTED
                && this.status != RequestStatus.CANCELLED
                && this.status != RequestStatus.REJECTED;
    }

    public boolean canBeCancelled() {
        return this.status != RequestStatus.CONTRACTED
                && this.status != RequestStatus.CANCELLED;
    }

    private void validateTransition(RequestStatus target) {
        boolean valid = switch (target) {
            case UNDER_REVIEW -> status == RequestStatus.PENDING;
            case APPROVED, REJECTED -> status == RequestStatus.UNDER_REVIEW;
            case CONTRACTED -> status == RequestStatus.APPROVED;
            case CANCELLED -> status == RequestStatus.PENDING || status == RequestStatus.UNDER_REVIEW;
            default -> false;
        };
        if (!valid) {
            throw new IllegalStateException(
                    "Transição inválida: " + status + " → " + target);
        }
    }
}

