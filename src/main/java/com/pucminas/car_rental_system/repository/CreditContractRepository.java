package com.pucminas.car_rental_system.repository;

import com.pucminas.car_rental_system.domain.entity.CreditContract;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface CreditContractRepository extends JpaRepository<CreditContract, Long> {

    @Query("SELECT cc FROM CreditContract cc LEFT JOIN FETCH cc.rentalRequest r WHERE r.id = :requestId")
    Optional<CreditContract> findByRentalRequestId(Long requestId);
}

