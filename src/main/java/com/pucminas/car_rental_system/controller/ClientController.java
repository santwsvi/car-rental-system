package com.pucminas.car_rental_system.controller;

import com.pucminas.car_rental_system.domain.dto.AddressDTO;
import com.pucminas.car_rental_system.domain.dto.ClientRequestDTO;
import com.pucminas.car_rental_system.domain.dto.ClientResponseDTO;
import com.pucminas.car_rental_system.domain.dto.EmployerDTO;
import com.pucminas.car_rental_system.exception.BusinessRuleException;
import com.pucminas.car_rental_system.service.ClientService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.View;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Controller MVC para operações CRUD de Cliente.
 *
 * <p>Segue o princípio SRP (Single Responsibility Principle):
 * apenas roteia requisições HTTP e delega toda lógica ao {@link ClientService}.
 * Nenhuma regra de negócio reside aqui.</p>
 *
 * <p>Micronaut HTTP: {@code @Controller} com {@code @View} para SSR via Thymeleaf.</p>
 *
 * <p><b>Nota técnica:</b> Micronaut não suporta binding automático de formulários
 * para objetos aninhados (nested objects) via notação de ponto.
 * O binding manual é a abordagem padrão para formulários complexos.</p>
 */
@Controller("/clients")
public class ClientController {

    private final ClientService clientService;
    private final Validator validator;

    public ClientController(ClientService clientService, Validator validator) {
        this.clientService = clientService;
        this.validator = validator;
    }

    // ═══════════════════════════════════════════════════════
    //  READ — Listagem
    // ═══════════════════════════════════════════════════════

    @Get
    @View("client/list")
    public Map<String, Object> list(@Nullable @QueryValue("success") String success) {
        Map<String, Object> model = new HashMap<>();
        List<ClientResponseDTO> clients = clientService.findAll();
        model.put("clients", clients);
        if (success != null && !success.isEmpty()) {
            model.put("successMessage", success);
        }
        return model;
    }

    // ═══════════════════════════════════════════════════════
    //  READ — Detalhes
    // ═══════════════════════════════════════════════════════

    @Get("/{id}")
    @View("client/detail")
    public Map<String, Object> detail(@PathVariable Long id) {
        Map<String, Object> model = new HashMap<>();
        ClientResponseDTO client = clientService.findById(id);
        model.put("client", client);
        return model;
    }

    // ═══════════════════════════════════════════════════════
    //  CREATE — Formulário + Processamento
    // ═══════════════════════════════════════════════════════

    @Get("/new")
    @View("client/form")
    public Map<String, Object> showCreateForm() {
        Map<String, Object> model = new HashMap<>();
        model.put("clientForm", new ClientRequestDTO());
        model.put("action", "create");
        return model;
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post
    public Object create(@Body Map<String, String> formData) {
        ClientRequestDTO dto = buildDtoFromForm(formData);

        Map<String, String> fieldErrors = validate(dto);
        if (!fieldErrors.isEmpty()) {
            Map<String, Object> model = new HashMap<>();
            model.put("clientForm", dto);
            model.put("action", "create");
            model.put("fieldErrors", fieldErrors);
            return new ModelAndView<>("client/form", model);
        }

        try {
            clientService.create(dto);
            return HttpResponse.redirect(URI.create("/clients?success=Cliente+cadastrado+com+sucesso!"));
        } catch (BusinessRuleException ex) {
            Map<String, Object> model = new HashMap<>();
            model.put("clientForm", dto);
            model.put("action", "create");
            model.put("errorMessage", ex.getMessage());
            return new ModelAndView<>("client/form", model);
        }
    }

    // ═══════════════════════════════════════════════════════
    //  UPDATE — Formulário + Processamento
    // ═══════════════════════════════════════════════════════

    @Get("/{id}/edit")
    @View("client/form")
    public Map<String, Object> showEditForm(@PathVariable Long id) {
        Map<String, Object> model = new HashMap<>();
        ClientRequestDTO dto = clientService.findByIdForEdit(id);
        model.put("clientForm", dto);
        model.put("clientId", id);
        model.put("action", "edit");
        return model;
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/{id}")
    public Object update(@PathVariable Long id, @Body Map<String, String> formData) {
        ClientRequestDTO dto = buildDtoFromForm(formData);

        Map<String, String> fieldErrors = validate(dto);
        if (!fieldErrors.isEmpty()) {
            Map<String, Object> model = new HashMap<>();
            model.put("clientForm", dto);
            model.put("clientId", id);
            model.put("action", "edit");
            model.put("fieldErrors", fieldErrors);
            return new ModelAndView<>("client/form", model);
        }

        try {
            clientService.update(id, dto);
            return HttpResponse.redirect(URI.create("/clients?success=Cliente+atualizado+com+sucesso!"));
        } catch (BusinessRuleException ex) {
            Map<String, Object> model = new HashMap<>();
            model.put("clientForm", dto);
            model.put("clientId", id);
            model.put("action", "edit");
            model.put("errorMessage", ex.getMessage());
            return new ModelAndView<>("client/form", model);
        }
    }

    // ═══════════════════════════════════════════════════════
    //  DELETE
    // ═══════════════════════════════════════════════════════

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/{id}/delete")
    public Object delete(@PathVariable Long id) {
        try {
            clientService.delete(id);
            return HttpResponse.redirect(URI.create("/clients?success=Cliente+removido+com+sucesso!"));
        } catch (BusinessRuleException ex) {
            Map<String, Object> model = new HashMap<>();
            ClientResponseDTO client = clientService.findById(id);
            model.put("client", client);
            model.put("errorMessage", ex.getMessage());
            return new ModelAndView<>("client/detail", model);
        }
    }

    // ═══════════════════════════════════════════════════════
    //  HELPERS — Binding manual + Validação
    // ═══════════════════════════════════════════════════════

    /**
     * Constrói o DTO a partir do mapa de formulário (flat key-value).
     * Micronaut não suporta binding automático de objetos aninhados
     * em form-urlencoded, por isso o binding manual é necessário.
     */
    private ClientRequestDTO buildDtoFromForm(Map<String, String> form) {
        AddressDTO address = AddressDTO.builder()
                .street(form.getOrDefault("address.street", ""))
                .number(form.getOrDefault("address.number", ""))
                .complement(form.getOrDefault("address.complement", ""))
                .city(form.getOrDefault("address.city", ""))
                .state(form.getOrDefault("address.state", ""))
                .zipCode(form.getOrDefault("address.zipCode", ""))
                .country(form.getOrDefault("address.country", "Brasil"))
                .build();

        List<EmployerDTO> employers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String empName = form.get("employers[" + i + "].name");
            String empIncome = form.get("employers[" + i + "].income");
            EmployerDTO emp = new EmployerDTO();
            emp.setName(empName);
            if (empIncome != null && !empIncome.isBlank()) {
                try {
                    emp.setIncome(new BigDecimal(empIncome));
                } catch (NumberFormatException ignored) { }
            }
            employers.add(emp);
        }

        return ClientRequestDTO.builder()
                .name(form.getOrDefault("name", ""))
                .email(form.getOrDefault("email", ""))
                .password(form.getOrDefault("password", ""))
                .rg(form.getOrDefault("rg", ""))
                .cpf(form.getOrDefault("cpf", ""))
                .profession(form.getOrDefault("profession", ""))
                .address(address)
                .employers(employers)
                .build();
    }

    private Map<String, String> validate(ClientRequestDTO dto) {
        Set<ConstraintViolation<ClientRequestDTO>> violations = validator.validate(dto);
        Map<String, String> errors = new LinkedHashMap<>();
        for (ConstraintViolation<ClientRequestDTO> v : violations) {
            errors.put(v.getPropertyPath().toString(), v.getMessage());
        }
        return errors;
    }
}
