package com.pucminas.car_rental_system.service;

import com.pucminas.car_rental_system.domain.dto.ClientRequestDTO;
import com.pucminas.car_rental_system.domain.dto.ClientResponseDTO;
import com.pucminas.car_rental_system.domain.entity.Client;
import com.pucminas.car_rental_system.domain.mapper.ClientMapper;
import com.pucminas.car_rental_system.exception.BusinessRuleException;
import com.pucminas.car_rental_system.exception.ResourceNotFoundException;
import com.pucminas.car_rental_system.repository.ClientRepository;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço de aplicação para operações CRUD de Cliente.
 *
 * <p>Padrão Facade (GoF): orquestra casos de uso, controla transações
 * {@code @Transactional} e valida invariantes de negócio antes de
 * qualquer escrita no banco.</p>
 *
 * <p>Micronaut DI: {@code @Singleton} — compile-time injection (sem reflexão).</p>
 */
@Singleton
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    // ═══════════════════════════════════════════════════════
    //  QUERIES
    // ═══════════════════════════════════════════════════════

    @Transactional
    public List<ClientResponseDTO> findAll() {
        return clientRepository.findAllWithEmployers().stream()
                .map(clientMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClientResponseDTO findById(Long id) {
        Client client = findClientEntityById(id);
        return clientMapper.toResponseDTO(client);
    }

    @Transactional
    public Client findClientEntityById(Long id) {
        return clientRepository.findByIdWithEmployers(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
    }

    @Transactional
    public ClientRequestDTO findByIdForEdit(Long id) {
        Client client = findClientEntityById(id);
        return clientMapper.toRequestDTO(client);
    }

    // ═══════════════════════════════════════════════════════
    //  COMMANDS
    // ═══════════════════════════════════════════════════════

    @Transactional
    public ClientResponseDTO create(ClientRequestDTO dto) {
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BusinessRuleException("Senha é obrigatória para cadastro de novo cliente.");
        }
        validateUniqueness(dto.getCpf(), dto.getEmail(), null);
        validateEmployersLimit(dto);

        Client client = clientMapper.toEntity(dto);
        Client saved = clientRepository.save(client);
        return clientMapper.toResponseDTO(saved);
    }

    @Transactional
    public ClientResponseDTO update(Long id, ClientRequestDTO dto) {
        Client existing = findClientEntityById(id);
        validateUniqueness(dto.getCpf(), dto.getEmail(), id);
        validateEmployersLimit(dto);

        clientMapper.updateEntity(dto, existing);
        Client saved = clientRepository.update(existing);
        return clientMapper.toResponseDTO(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente", id);
        }
        clientRepository.deleteById(id);
    }

    // ═══════════════════════════════════════════════════════
    //  VALIDAÇÕES DE NEGÓCIO
    // ═══════════════════════════════════════════════════════

    private void validateUniqueness(String cpf, String email, Long excludeId) {
        clientRepository.findByCpf(cpf).ifPresent(existing -> {
            if (!existing.getId().equals(excludeId)) {
                throw new BusinessRuleException("Já existe um cliente cadastrado com o CPF: " + cpf);
            }
        });
        clientRepository.findByEmail(email).ifPresent(existing -> {
            if (!existing.getId().equals(excludeId)) {
                throw new BusinessRuleException("Já existe um cliente cadastrado com o e-mail: " + email);
            }
        });
    }

    private void validateEmployersLimit(ClientRequestDTO dto) {
        if (dto.getEmployers() != null) {
            long validEmployers = dto.getEmployers().stream()
                    .filter(e -> e.getName() != null && !e.getName().isBlank())
                    .count();
            if (validEmployers > Client.MAX_EMPLOYERS) {
                throw new BusinessRuleException(
                        "Cliente não pode possuir mais de " + Client.MAX_EMPLOYERS + " empregadores.");
            }
        }
    }
}
