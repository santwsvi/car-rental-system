package com.pucminas.car_rental_system.service;

import com.pucminas.car_rental_system.domain.dto.CreditContractDTO;
import com.pucminas.car_rental_system.domain.entity.CreditContract;
import com.pucminas.car_rental_system.domain.entity.RentalRequest;
import com.pucminas.car_rental_system.domain.enums.RequestStatus;
import com.pucminas.car_rental_system.exception.BusinessRuleException;
import com.pucminas.car_rental_system.exception.ResourceNotFoundException;
import com.pucminas.car_rental_system.repository.CreditContractRepository;
import com.pucminas.car_rental_system.repository.RentalRequestRepository;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

/**
 * Serviço para concessão de contratos de crédito (História 8).
 * Exclusivo para agentes com perfil de Banco.
 */
@Singleton
public class CreditContractService {

    private final CreditContractRepository creditContractRepository;
    private final RentalRequestRepository rentalRequestRepository;

    public CreditContractService(CreditContractRepository creditContractRepository,
                                  RentalRequestRepository rentalRequestRepository) {
        this.creditContractRepository = creditContractRepository;
        this.rentalRequestRepository = rentalRequestRepository;
    }

    @Transactional
    public CreditContract findByRequestId(Long requestId) {
        return creditContractRepository.findByRentalRequestId(requestId).orElse(null);
    }

    /**
     * História 8: Banco concede contrato de crédito.
     * Só pode ser associado a pedidos com parecer financeiro positivo (APPROVED).
     */
    @Transactional
    public CreditContract issueCredit(CreditContractDTO dto) {
        RentalRequest request = rentalRequestRepository.findByIdWithClient(dto.getRentalRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido de aluguel", dto.getRentalRequestId()));

        if (request.getStatus() != RequestStatus.APPROVED) {
            throw new BusinessRuleException(
                    "Crédito só pode ser concedido a pedidos com parecer financeiro positivo (aprovados).");
        }

        // Verificar se já existe crédito
        if (creditContractRepository.findByRentalRequestId(request.getId()).isPresent()) {
            throw new BusinessRuleException("Este pedido já possui um contrato de crédito.");
        }

        CreditContract credit = CreditContract.builder()
                .rentalRequest(request)
                .bankName(dto.getBankName())
                .creditValue(dto.getCreditValue())
                .interestRate(dto.getInterestRate())
                .build();

        return creditContractRepository.save(credit);
    }
}

