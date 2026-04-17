package com.pucminas.car_rental_system.domain.enums;

/**
 * Perfis de usuário do sistema — define o nível de autorização.
 *
 * <p>Utilizado como discriminador RBAC (Role-Based Access Control)
 * na camada de segurança e como discriminador de herança JPA.</p>
 *
 * @see com.pucminas.car_rental_system.domain.entity.User
 */
public enum UserRole {

    CLIENT,
    AGENT_COMPANY,
    AGENT_BANK
}

