package com.pucminas.car_rental_system.domain.mapper;

import com.pucminas.car_rental_system.domain.dto.AddressDTO;
import com.pucminas.car_rental_system.domain.dto.ClientRequestDTO;
import com.pucminas.car_rental_system.domain.dto.ClientResponseDTO;
import com.pucminas.car_rental_system.domain.dto.ClientResponseDTO;
import com.pucminas.car_rental_system.domain.dto.EmployerDTO;
import com.pucminas.car_rental_system.domain.entity.Address;
import com.pucminas.car_rental_system.domain.entity.Client;
import com.pucminas.car_rental_system.domain.entity.Employer;
import com.pucminas.car_rental_system.domain.enums.UserRole;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper responsável pela transformação bidirecional entre
 * {@link Client} (Entity) e seus DTOs.
 *
 * <p>Responsabilidade única de conversão — facilita testes unitários
 * e evolução independente do modelo de domínio vs contrato de API.</p>
 */
@Singleton
public class ClientMapper {

    /**
     * Converte um DTO de request em uma entidade Client.
     *
     * @param dto dados de entrada do formulário
     * @return entidade Client populada (sem ID)
     */
    public Client toEntity(ClientRequestDTO dto) {
        if (dto == null) return null;

        Client client = Client.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .passwordHash(dto.getPassword()) // Em produção: BCrypt.encode()
                .role(UserRole.CLIENT)
                .rg(dto.getRg())
                .cpf(dto.getCpf())
                .profession(dto.getProfession())
                .address(toAddressEntity(dto.getAddress()))
                .build();

        if (dto.getEmployers() != null) {
            dto.getEmployers().stream()
                    .filter(e -> e.getName() != null && !e.getName().isBlank())
                    .forEach(empDto -> {
                        Employer employer = toEmployerEntity(empDto);
                        client.addEmployer(employer);
                    });
        }

        return client;
    }

    /**
     * Atualiza uma entidade Client existente com os dados do DTO.
     *
     * @param dto    dados atualizados
     * @param client entidade existente a ser modificada
     */
    public void updateEntity(ClientRequestDTO dto, Client client) {
        if (dto == null || client == null) return;

        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            client.setPasswordHash(dto.getPassword()); // Em produção: BCrypt.encode()
        }
        client.setRg(dto.getRg());
        client.setCpf(dto.getCpf());
        client.setProfession(dto.getProfession());
        client.setAddress(toAddressEntity(dto.getAddress()));

        // Limpa empregadores antigos e adiciona novos
        client.getEmployers().clear();
        if (dto.getEmployers() != null) {
            dto.getEmployers().stream()
                    .filter(e -> e.getName() != null && !e.getName().isBlank())
                    .forEach(empDto -> {
                        Employer employer = toEmployerEntity(empDto);
                        client.addEmployer(employer);
                    });
        }
    }

    /**
     * Converte uma entidade Client em DTO de resposta.
     *
     * @param client entidade persistida
     * @return DTO com dados para exibição
     */
    public ClientResponseDTO toResponseDTO(Client client) {
        if (client == null) return null;

        return ClientResponseDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .email(client.getEmail())
                .rg(client.getRg())
                .cpf(client.getCpf())
                .profession(client.getProfession())
                .address(toAddressDTO(client.getAddress()))
                .employers(toEmployerDTOList(client.getEmployers()))
                .createdAt(client.getCreatedAt())
                .build();
    }

    /**
     * Converte uma entidade Client em DTO de request (para pré-popular formulário de edição).
     *
     * @param client entidade persistida
     * @return DTO com dados para formulário
     */
    public ClientRequestDTO toRequestDTO(Client client) {
        if (client == null) return null;

        ClientRequestDTO dto = ClientRequestDTO.builder()
                .name(client.getName())
                .email(client.getEmail())
                .password("")
                .rg(client.getRg())
                .cpf(client.getCpf())
                .profession(client.getProfession())
                .address(toAddressDTO(client.getAddress()) != null
                        ? toAddressDTO(client.getAddress()) : new AddressDTO())
                .employers(toEmployerDTOList(client.getEmployers()))
                .build();
        dto.ensureEmployerSlots();
        return dto;
    }

    // ─── Conversores auxiliares ─────────────────────────────

    private Address toAddressEntity(AddressDTO dto) {
        if (dto == null) return null;
        return Address.builder()
                .street(dto.getStreet())
                .number(dto.getNumber())
                .complement(dto.getComplement())
                .city(dto.getCity())
                .state(dto.getState())
                .zipCode(dto.getZipCode())
                .country(dto.getCountry())
                .build();
    }

    private AddressDTO toAddressDTO(Address address) {
        if (address == null) return null;
        return AddressDTO.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .country(address.getCountry())
                .build();
    }

    private Employer toEmployerEntity(EmployerDTO dto) {
        if (dto == null) return null;
        return Employer.builder()
                .name(dto.getName())
                .income(dto.getIncome())
                .build();
    }

    private EmployerDTO toEmployerDTO(Employer employer) {
        if (employer == null) return null;
        return EmployerDTO.builder()
                .id(employer.getId())
                .name(employer.getName())
                .income(employer.getIncome())
                .build();
    }

    private List<EmployerDTO> toEmployerDTOList(List<Employer> employers) {
        if (employers == null) return Collections.emptyList();
        return employers.stream()
                .map(this::toEmployerDTO)
                .collect(Collectors.toList());
    }
}





