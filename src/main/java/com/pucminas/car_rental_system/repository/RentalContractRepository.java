package com.pucminas.car_rental_system.repository;

import com.pucminas.car_rental_system.domain.entity.RentalContract;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface RentalContractRepository extends JpaRepository<RentalContract, Long> {

    @Query("SELECT rc FROM RentalContract rc LEFT JOIN FETCH rc.rentalRequest r LEFT JOIN FETCH r.client LEFT JOIN FETCH r.car WHERE rc.id = :id")
    Optional<RentalContract> findByIdWithDetails(Long id);

    @Query("SELECT rc FROM RentalContract rc LEFT JOIN FETCH rc.rentalRequest r WHERE r.id = :requestId")
    Optional<RentalContract> findByRentalRequestId(Long requestId);
}

