package com.pucminas.car_rental_system.controller;

import com.pucminas.car_rental_system.domain.dto.RentalRequestDTO;
import com.pucminas.car_rental_system.domain.dto.RentalRequestResponseDTO;
import com.pucminas.car_rental_system.exception.BusinessRuleException;
import com.pucminas.car_rental_system.service.ClientService;
import com.pucminas.car_rental_system.service.RentalRequestService;
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

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller MVC para operações de Pedidos de Aluguel (Histórias 2-5).
 */
@Controller("/rental-requests")
public class RentalRequestController {

    private final RentalRequestService rentalRequestService;
    private final ClientService clientService;

    public RentalRequestController(RentalRequestService rentalRequestService,
                                    ClientService clientService) {
        this.rentalRequestService = rentalRequestService;
        this.clientService = clientService;
    }

    // ═══ LIST — todos os pedidos ═══

    @Get
    @View("rental-request/list")
    public Map<String, Object> list(@Nullable @QueryValue("success") String success,
                                     @Nullable @QueryValue("clientId") Long clientId) {
        Map<String, Object> model = new HashMap<>();
        List<RentalRequestResponseDTO> requests;

        if (clientId != null) {
            requests = rentalRequestService.findByClientId(clientId);
            model.put("clientId", clientId);
        } else {
            requests = rentalRequestService.findAll();
        }

        model.put("requests", requests);
        if (success != null && !success.isEmpty()) {
            model.put("successMessage", success);
        }
        return model;
    }

    // ═══ DETAIL ═══

    @Get("/{id}")
    @View("rental-request/detail")
    public Map<String, Object> detail(@PathVariable Long id) {
        Map<String, Object> model = new HashMap<>();
        model.put("request", rentalRequestService.findById(id));
        return model;
    }

    // ═══ CREATE ═══

    @Get("/new")
    @View("rental-request/form")
    public Map<String, Object> showCreateForm(@Nullable @QueryValue("clientId") Long clientId) {
        Map<String, Object> model = new HashMap<>();
        model.put("requestForm", new RentalRequestDTO());
        model.put("action", "create");
        model.put("clients", clientService.findAll());
        if (clientId != null) {
            RentalRequestDTO dto = new RentalRequestDTO();
            dto.setClientId(clientId);
            model.put("requestForm", dto);
        }
        return model;
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post
    public Object create(@Body Map<String, String> formData) {
        RentalRequestDTO dto = buildDtoFromForm(formData);
        Long clientId = parseClientId(formData);

        if (clientId == null) {
            Map<String, Object> model = new HashMap<>();
            model.put("requestForm", dto);
            model.put("action", "create");
            model.put("clients", clientService.findAll());
            model.put("errorMessage", "Selecione um cliente.");
            return new ModelAndView<>("rental-request/form", model);
        }

        try {
            rentalRequestService.create(clientId, dto);
            return HttpResponse.redirect(URI.create("/rental-requests?success=Pedido+criado+com+sucesso!"));
        } catch (BusinessRuleException ex) {
            Map<String, Object> model = new HashMap<>();
            model.put("requestForm", dto);
            model.put("action", "create");
            model.put("clients", clientService.findAll());
            model.put("errorMessage", ex.getMessage());
            return new ModelAndView<>("rental-request/form", model);
        }
    }

    // ═══ EDIT ═══

    @Get("/{id}/edit")
    @View("rental-request/form")
    public Map<String, Object> showEditForm(@PathVariable Long id) {
        Map<String, Object> model = new HashMap<>();
        model.put("requestForm", rentalRequestService.findByIdForEdit(id));
        model.put("requestId", id);
        model.put("action", "edit");
        model.put("clients", clientService.findAll());
        return model;
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/{id}")
    public Object update(@PathVariable Long id, @Body Map<String, String> formData) {
        RentalRequestDTO dto = buildDtoFromForm(formData);

        try {
            rentalRequestService.update(id, dto);
            return HttpResponse.redirect(URI.create("/rental-requests?success=Pedido+atualizado+com+sucesso!"));
        } catch (BusinessRuleException ex) {
            Map<String, Object> model = new HashMap<>();
            model.put("requestForm", dto);
            model.put("requestId", id);
            model.put("action", "edit");
            model.put("clients", clientService.findAll());
            model.put("errorMessage", ex.getMessage());
            return new ModelAndView<>("rental-request/form", model);
        }
    }

    // ═══ CANCEL ═══

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/{id}/cancel")
    public HttpResponse<?> cancel(@PathVariable Long id) {
        rentalRequestService.cancel(id);
        return HttpResponse.redirect(URI.create("/rental-requests?success=Pedido+cancelado+com+sucesso!"));
    }

    // ═══ HELPERS ═══

    private RentalRequestDTO buildDtoFromForm(Map<String, String> form) {
        RentalRequestDTO dto = new RentalRequestDTO();

        String clientIdStr = form.get("clientId");
        if (clientIdStr != null && !clientIdStr.isBlank()) {
            dto.setClientId(Long.parseLong(clientIdStr));
        }

        String start = form.get("startDate");
        if (start != null && !start.isBlank()) {
            dto.setStartDate(LocalDate.parse(start));
        }

        String end = form.get("endDate");
        if (end != null && !end.isBlank()) {
            dto.setEndDate(LocalDate.parse(end));
        }

        dto.setNotes(form.getOrDefault("notes", ""));
        return dto;
    }

    private Long parseClientId(Map<String, String> form) {
        String clientIdStr = form.get("clientId");
        if (clientIdStr != null && !clientIdStr.isBlank()) {
            try {
                return Long.parseLong(clientIdStr);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}

