package com.pucminas.car_rental_system.repository;

import com.pucminas.car_rental_system.domain.entity.RentalRequest;
import com.pucminas.car_rental_system.domain.enums.RequestStatus;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {

    @Query("SELECT r FROM RentalRequest r LEFT JOIN FETCH r.client LEFT JOIN FETCH r.car WHERE r.client.id = :clientId ORDER BY r.createdAt DESC")
    List<RentalRequest> findByClientId(Long clientId);

    @Query("SELECT r FROM RentalRequest r LEFT JOIN FETCH r.client LEFT JOIN FETCH r.car ORDER BY r.createdAt DESC")
    List<RentalRequest> findAllWithClient();

    @Query("SELECT r FROM RentalRequest r LEFT JOIN FETCH r.client LEFT JOIN FETCH r.car WHERE r.id = :id")
    Optional<RentalRequest> findByIdWithClient(Long id);

    List<RentalRequest> findByStatus(RequestStatus status);
}

