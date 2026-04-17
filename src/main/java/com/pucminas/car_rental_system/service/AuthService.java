package com.pucminas.car_rental_system.service;

import com.pucminas.car_rental_system.domain.entity.User;
import com.pucminas.car_rental_system.repository.BankRepository;
import com.pucminas.car_rental_system.repository.ClientRepository;
import com.pucminas.car_rental_system.repository.CompanyRepository;
import jakarta.inject.Singleton;

import java.util.Optional;

/**
 * Serviço de autenticação — busca usuário por e-mail em todas as tabelas
 * e valida a senha (comparação simples; em produção usar BCrypt).
 */
@Singleton
public class AuthService {

    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;
    private final BankRepository bankRepository;

    public AuthService(ClientRepository clientRepository,
                       CompanyRepository companyRepository,
                       BankRepository bankRepository) {
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
        this.bankRepository = bankRepository;
    }

    /**
     * Tenta autenticar com e-mail e senha.
     * Retorna o User se as credenciais forem válidas, senão Optional vazio.
     */
    public Optional<User> authenticate(String email, String password) {
        // Busca em todas as tabelas de usuário
        Optional<? extends User> user = clientRepository.findByEmail(email);
        if (user.isEmpty()) {
            user = companyRepository.findByEmail(email);
        }
        if (user.isEmpty()) {
            user = bankRepository.findByEmail(email);
        }

        // Valida senha (comparação direta — em produção usar hash)
        return user.filter(u -> u.getPasswordHash().equals(password))
                   .map(u -> (User) u);
    }

    /**
     * Busca usuário por ID em todas as tabelas.
     */
    public Optional<User> findById(Long id) {
        Optional<? extends User> user = clientRepository.findById(id);
        if (user.isEmpty()) {
            user = companyRepository.findById(id);
        }
        if (user.isEmpty()) {
            user = bankRepository.findById(id);
        }
        return user.map(u -> (User) u);
    }
}

