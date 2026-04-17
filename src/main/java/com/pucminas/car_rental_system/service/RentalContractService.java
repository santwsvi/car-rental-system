package com.pucminas.car_rental_system.service;

import com.pucminas.car_rental_system.domain.dto.CarDTO;
import com.pucminas.car_rental_system.domain.dto.ExecuteContractDTO;
import com.pucminas.car_rental_system.domain.entity.Car;
import com.pucminas.car_rental_system.domain.entity.RentalContract;
import com.pucminas.car_rental_system.domain.entity.RentalRequest;
import com.pucminas.car_rental_system.domain.enums.RequestStatus;
import com.pucminas.car_rental_system.domain.mapper.RentalRequestMapper;
import com.pucminas.car_rental_system.exception.BusinessRuleException;
import com.pucminas.car_rental_system.exception.ResourceNotFoundException;
import com.pucminas.car_rental_system.repository.CarRepository;
import com.pucminas.car_rental_system.repository.RentalContractRepository;
import com.pucminas.car_rental_system.repository.RentalRequestRepository;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

/**
 * Serviço para execução de contratos de aluguel (História 9).
 */
@Singleton
public class RentalContractService {

    private final RentalContractRepository contractRepository;
    private final RentalRequestRepository requestRepository;
    private final CarRepository carRepository;
    private final RentalRequestMapper mapper;

    public RentalContractService(RentalContractRepository contractRepository,
                                  RentalRequestRepository requestRepository,
                                  CarRepository carRepository,
                                  RentalRequestMapper mapper) {
        this.contractRepository = contractRepository;
        this.requestRepository = requestRepository;
        this.carRepository = carRepository;
        this.mapper = mapper;
    }

    @Transactional
    public RentalContract findById(Long id) {
        return contractRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrato de aluguel", id));
    }

    @Transactional
    public RentalContract findByRequestId(Long requestId) {
        return contractRepository.findByRentalRequestId(requestId).orElse(null);
    }

    /**
     * História 9: Executa contrato de aluguel vinculando dados do automóvel.
     */
    @Transactional
    public RentalContract executeContract(ExecuteContractDTO dto) {
        RentalRequest request = requestRepository.findByIdWithClient(dto.getRentalRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido de aluguel", dto.getRentalRequestId()));

        if (request.getStatus() != RequestStatus.APPROVED) {
            throw new BusinessRuleException("Apenas pedidos aprovados podem ter contrato executado.");
        }

        // Verificar se já existe contrato
        if (contractRepository.findByRentalRequestId(request.getId()).isPresent()) {
            throw new BusinessRuleException("Este pedido já possui um contrato executado.");
        }

        // Validar dados do carro
        CarDTO carDTO = dto.getCar();
        if (carRepository.existsByPlate(carDTO.getPlate())) {
            throw new BusinessRuleException("Já existe um veículo cadastrado com a placa: " + carDTO.getPlate());
        }
        if (carRepository.existsByRegistration(carDTO.getRegistration())) {
            throw new BusinessRuleException("Já existe um veículo cadastrado com a matrícula: " + carDTO.getRegistration());
        }

        // Criar carro
        Car car = mapper.toCarEntity(carDTO);
        car = carRepository.save(car);

        // Vincular carro ao pedido
        request.setCar(car);
        request.markContracted();
        requestRepository.update(request);

        // Criar contrato
        RentalContract contract = RentalContract.builder()
                .rentalRequest(request)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalValue(dto.getTotalValue())
                .carOwnerType(dto.getCarOwnerType())
                .carOwnerName(dto.getCarOwnerName())
                .build();

        return contractRepository.save(contract);
    }
}

