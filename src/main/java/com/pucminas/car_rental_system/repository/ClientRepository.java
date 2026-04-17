package com.pucminas.car_rental_system.repository;

import com.pucminas.car_rental_system.domain.entity.Client;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório de acesso a dados para a entidade {@link Client}.
 *
 * <p>Padrão Repository (DDD — Evans) via Micronaut Data JPA:
 * isola o domínio do mecanismo de persistência.
 * Compile-time query generation — sem reflexão em runtime.</p>
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Busca cliente por CPF (campo único).
     */
    Optional<Client> findByCpf(String cpf);

    /**
     * Busca cliente por e-mail (campo único).
     */
    Optional<Client> findByEmail(String email);

    /**
     * Verifica existência por CPF — evita SELECT completo para validação.
     */
    boolean existsByCpf(String cpf);

    /**
     * Verifica existência por e-mail — evita SELECT completo para validação.
     */
    boolean existsByEmail(String email);

    /**
     * Lista todos os clientes com fetch de empregadores — evita N+1 Problem.
     */
    @Query("SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.employers")
    List<Client> findAllWithEmployers();

    /**
     * Busca cliente por ID com fetch de empregadores — evita N+1 Problem.
     */
    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.employers WHERE c.id = :id")
    Optional<Client> findByIdWithEmployers(Long id);
}


