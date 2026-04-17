package com.pucminas.car_rental_system.domain.entity;

import com.pucminas.car_rental_system.domain.enums.UserRole;
import io.micronaut.core.annotation.Introspected;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;

/**
 * Raiz abstrata da hierarquia de usuários — {@code @MappedSuperclass} JPA.
 *
 * <p>Campos comuns a todos os usuários do sistema são definidos aqui
 * e incorporados nas tabelas das subclasses concretas.</p>
 *
 * <p><b>Nota técnica:</b> getters/setters são escritos manualmente (sem Lombok)
 * para garantir visibilidade ao annotation processor compile-time do Micronaut Data,
 * que precisa enxergar {@code @Id} na cadeia de introspecção.</p>
 *
 * <p>Hierarquia:</p>
 * <pre>
 *   User (abstract — @MappedSuperclass)
 *     ├── Client
 *     └── Agent (abstract)
 *           ├── Company
 *           └── Bank
 * </pre>
 *
 * @see Client
 */
@MappedSuperclass
@Introspected
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public User() {}

    public User(Long id, String email, String passwordHash, UserRole role, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Nome de exibição — implementação polimórfica por subclasse.
     */
    public abstract String getDisplayName();

    // ─── Getters & Setters (manual — visibilidade ao Micronaut AP) ─────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}




