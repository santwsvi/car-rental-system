package com.pucminas.car_rental_system.repository;

import com.pucminas.car_rental_system.domain.entity.Car;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findByPlate(String plate);
    Optional<Car> findByRegistration(String registration);
    boolean existsByPlate(String plate);
    boolean existsByRegistration(String registration);
}

