package com.pucminas.car_rental_system.domain.mapper;

import com.pucminas.car_rental_system.domain.dto.CarDTO;
import com.pucminas.car_rental_system.domain.dto.RentalRequestDTO;
import com.pucminas.car_rental_system.domain.dto.RentalRequestResponseDTO;
import com.pucminas.car_rental_system.domain.entity.Car;
import com.pucminas.car_rental_system.domain.entity.RentalRequest;
import jakarta.inject.Singleton;

/**
 * Mapper para RentalRequest ↔ DTOs.
 */
@Singleton
public class RentalRequestMapper {

    public RentalRequestResponseDTO toResponseDTO(RentalRequest entity) {
        if (entity == null) return null;

        CarDTO carDTO = null;
        if (entity.getCar() != null) {
            Car car = entity.getCar();
            carDTO = CarDTO.builder()
                    .id(car.getId())
                    .registration(car.getRegistration())
                    .year(car.getYear())
                    .brand(car.getBrand())
                    .model(car.getModel())
                    .plate(car.getPlate())
                    .build();
        }

        return RentalRequestResponseDTO.builder()
                .id(entity.getId())
                .clientId(entity.getClient() != null ? entity.getClient().getId() : null)
                .clientName(entity.getClient() != null ? entity.getClient().getName() : null)
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus().name())
                .notes(entity.getNotes())
                .evaluatedBy(entity.getEvaluatedBy())
                .evaluatedByRole(entity.getEvaluatedByRole() != null ? entity.getEvaluatedByRole().name() : null)
                .car(carDTO)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .canModify(entity.canBeModified())
                .canCancel(entity.canBeCancelled())
                .build();
    }

    public RentalRequestDTO toRequestDTO(RentalRequest entity) {
        if (entity == null) return null;
        return RentalRequestDTO.builder()
                .clientId(entity.getClient() != null ? entity.getClient().getId() : null)
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .notes(entity.getNotes())
                .build();
    }

    public Car toCarEntity(CarDTO dto) {
        if (dto == null) return null;
        return Car.builder()
                .registration(dto.getRegistration())
                .year(dto.getYear())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .plate(dto.getPlate())
                .build();
    }
}

