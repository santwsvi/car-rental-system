package com.pucminas.car_rental_system.repository;

import com.pucminas.car_rental_system.domain.entity.Bank;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    Optional<Bank> findByCnpj(String cnpj);
    Optional<Bank> findByEmail(String email);
}


