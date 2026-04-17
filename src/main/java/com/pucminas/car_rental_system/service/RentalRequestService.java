package com.pucminas.car_rental_system.service;

import com.pucminas.car_rental_system.domain.dto.RentalRequestDTO;
import com.pucminas.car_rental_system.domain.dto.RentalRequestResponseDTO;
import com.pucminas.car_rental_system.domain.entity.Client;
import com.pucminas.car_rental_system.domain.entity.RentalRequest;
import com.pucminas.car_rental_system.domain.enums.RequestStatus;
import com.pucminas.car_rental_system.domain.enums.UserRole;
import com.pucminas.car_rental_system.domain.mapper.RentalRequestMapper;
import com.pucminas.car_rental_system.exception.BusinessRuleException;
import com.pucminas.car_rental_system.exception.ResourceNotFoundException;
import com.pucminas.car_rental_system.repository.RentalRequestRepository;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para operações de Pedido de Aluguel.
 * Controla a máquina de estados e valida invariantes de negócio.
 */
@Singleton
public class RentalRequestService {

    private final RentalRequestRepository rentalRequestRepository;
    private final ClientService clientService;
    private final RentalRequestMapper mapper;

    public RentalRequestService(RentalRequestRepository rentalRequestRepository,
                                 ClientService clientService,
                                 RentalRequestMapper mapper) {
        this.rentalRequestRepository = rentalRequestRepository;
        this.clientService = clientService;
        this.mapper = mapper;
    }

    // ═══ QUERIES ═══

    @Transactional
    public List<RentalRequestResponseDTO> findAll() {
        return rentalRequestRepository.findAllWithClient().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<RentalRequestResponseDTO> findByClientId(Long clientId) {
        return rentalRequestRepository.findByClientId(clientId).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public RentalRequestResponseDTO findById(Long id) {
        RentalRequest entity = findEntityById(id);
        return mapper.toResponseDTO(entity);
    }

    @Transactional
    public RentalRequest findEntityById(Long id) {
        return rentalRequestRepository.findByIdWithClient(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido de aluguel", id));
    }

    @Transactional
    public RentalRequestDTO findByIdForEdit(Long id) {
        RentalRequest entity = findEntityById(id);
        if (!entity.canBeModified()) {
            throw new BusinessRuleException("Este pedido não pode mais ser modificado.");
        }
        return mapper.toRequestDTO(entity);
    }

    // ═══ COMMANDS ═══

    /** História 2: Cliente cria pedido de aluguel */
    @Transactional
    public RentalRequestResponseDTO create(Long clientId, RentalRequestDTO dto) {
        Client client = clientService.findClientEntityById(clientId);

        validateDates(dto);

        RentalRequest request = RentalRequest.builder()
                .client(client)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .notes(dto.getNotes())
                .status(RequestStatus.PENDING)
                .build();

        RentalRequest saved = rentalRequestRepository.save(request);
        return mapper.toResponseDTO(saved);
    }

    /** História 4: Cliente modifica pedido (somente se não executado) */
    @Transactional
    public RentalRequestResponseDTO update(Long id, RentalRequestDTO dto) {
        RentalRequest existing = findEntityById(id);

        if (!existing.canBeModified()) {
            throw new BusinessRuleException("Este pedido não pode mais ser modificado.");
        }

        validateDates(dto);

        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setNotes(dto.getNotes());

        // Se já estava em análise, retorna para PENDING (reavaliação)
        if (existing.getStatus() == RequestStatus.UNDER_REVIEW) {
            existing.setStatus(RequestStatus.PENDING);
            existing.setEvaluatedBy(null);
            existing.setEvaluatedByRole(null);
        }

        RentalRequest saved = rentalRequestRepository.update(existing);
        return mapper.toResponseDTO(saved);
    }

    /** História 5: Cliente cancela pedido */
    @Transactional
    public void cancel(Long id) {
        RentalRequest request = findEntityById(id);
        if (!request.canBeCancelled()) {
            throw new BusinessRuleException("Este pedido não pode ser cancelado.");
        }
        request.cancel();
        rentalRequestRepository.update(request);
    }

    /** História 6: Agente submete pedido para análise */
    @Transactional
    public void submitForReview(Long id) {
        RentalRequest request = findEntityById(id);
        try {
            request.submitForReview();
        } catch (IllegalStateException e) {
            throw new BusinessRuleException(e.getMessage());
        }
        rentalRequestRepository.update(request);
    }

    /** História 6: Agente aprova pedido */
    @Transactional
    public void approve(Long id, String agentName, UserRole agentRole) {
        RentalRequest request = findEntityById(id);
        try {
            request.approve(agentName, agentRole);
        } catch (IllegalStateException e) {
            throw new BusinessRuleException(e.getMessage());
        }
        rentalRequestRepository.update(request);
    }

    /** História 6: Agente rejeita pedido */
    @Transactional
    public void reject(Long id, String agentName, UserRole agentRole) {
        RentalRequest request = findEntityById(id);
        try {
            request.reject(agentName, agentRole);
        } catch (IllegalStateException e) {
            throw new BusinessRuleException(e.getMessage());
        }
        rentalRequestRepository.update(request);
    }

    /** História 7: Agente modifica detalhes do pedido */
    @Transactional
    public void agentModify(Long id, RentalRequestDTO dto, String agentName) {
        RentalRequest existing = findEntityById(id);
        if (!existing.canBeModified()) {
            throw new BusinessRuleException("Este pedido não pode mais ser modificado.");
        }

        validateDates(dto);
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        if (dto.getNotes() != null) {
            String updatedNotes = (existing.getNotes() != null ? existing.getNotes() + "\n" : "")
                    + "[Modificado por " + agentName + "]: " + dto.getNotes();
            existing.setNotes(updatedNotes);
        }

        rentalRequestRepository.update(existing);
    }

    // ═══ VALIDAÇÕES ═══

    private void validateDates(RentalRequestDTO dto) {
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            if (dto.getEndDate().isBefore(dto.getStartDate())) {
                throw new BusinessRuleException("Data de término deve ser posterior à data de início.");
            }
        }
    }
}

