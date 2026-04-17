package com.pucminas.car_rental_system.controller;

import com.pucminas.car_rental_system.domain.dto.RentalRequestDTO;
import com.pucminas.car_rental_system.domain.dto.RentalRequestResponseDTO;
import com.pucminas.car_rental_system.domain.enums.UserRole;
import com.pucminas.car_rental_system.exception.BusinessRuleException;
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
 * Controller para operações de agentes (Histórias 6-7).
 * Em produção, este controller seria protegido por autenticação de agente.
 */
@Controller("/agent")
public class AgentController {

    private final RentalRequestService rentalRequestService;

    public AgentController(RentalRequestService rentalRequestService) {
        this.rentalRequestService = rentalRequestService;
    }

    // ═══ DASHBOARD — Lista pedidos para avaliação ═══

    @Get("/requests")
    @View("agent/request-list")
    public Map<String, Object> listRequests(@Nullable @QueryValue("success") String success) {
        Map<String, Object> model = new HashMap<>();
        model.put("requests", rentalRequestService.findAll());
        if (success != null && !success.isEmpty()) {
            model.put("successMessage", success);
        }
        return model;
    }

    @Get("/requests/{id}")
    @View("agent/request-detail")
    public Map<String, Object> requestDetail(@PathVariable Long id) {
        Map<String, Object> model = new HashMap<>();
        model.put("request", rentalRequestService.findById(id));
        return model;
    }

    // ═══ SUBMIT FOR REVIEW ═══

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/requests/{id}/submit-review")
    public Object submitForReview(@PathVariable Long id) {
        try {
            rentalRequestService.submitForReview(id);
            return HttpResponse.redirect(URI.create("/agent/requests?success=Pedido+enviado+para+análise!"));
        } catch (BusinessRuleException ex) {
            Map<String, Object> model = new HashMap<>();
            model.put("request", rentalRequestService.findById(id));
            model.put("errorMessage", ex.getMessage());
            return new ModelAndView<>("agent/request-detail", model);
        }
    }

    // ═══ APPROVE ═══

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/requests/{id}/approve")
    public Object approve(@PathVariable Long id, @Body Map<String, String> formData) {
        String agentName = formData.getOrDefault("agentName", "Agente");
        String roleStr = formData.getOrDefault("agentRole", "AGENT_COMPANY");

        try {
            UserRole role = UserRole.valueOf(roleStr);
            rentalRequestService.approve(id, agentName, role);
            return HttpResponse.redirect(URI.create("/agent/requests?success=Pedido+aprovado!"));
        } catch (BusinessRuleException ex) {
            Map<String, Object> model = new HashMap<>();
            model.put("request", rentalRequestService.findById(id));
            model.put("errorMessage", ex.getMessage());
            return new ModelAndView<>("agent/request-detail", model);
        }
    }

    // ═══ REJECT ═══

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/requests/{id}/reject")
    public Object reject(@PathVariable Long id, @Body Map<String, String> formData) {
        String agentName = formData.getOrDefault("agentName", "Agente");
        String roleStr = formData.getOrDefault("agentRole", "AGENT_COMPANY");

        try {
            UserRole role = UserRole.valueOf(roleStr);
            rentalRequestService.reject(id, agentName, role);
            return HttpResponse.redirect(URI.create("/agent/requests?success=Pedido+rejeitado."));
        } catch (BusinessRuleException ex) {
            Map<String, Object> model = new HashMap<>();
            model.put("request", rentalRequestService.findById(id));
            model.put("errorMessage", ex.getMessage());
            return new ModelAndView<>("agent/request-detail", model);
        }
    }

    // ═══ AGENT MODIFY ═══

    @Get("/requests/{id}/edit")
    @View("agent/request-edit")
    public Map<String, Object> showEditForm(@PathVariable Long id) {
        Map<String, Object> model = new HashMap<>();
        model.put("requestForm", rentalRequestService.findByIdForEdit(id));
        model.put("requestId", id);
        model.put("request", rentalRequestService.findById(id));
        return model;
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/requests/{id}/edit")
    public Object agentModify(@PathVariable Long id, @Body Map<String, String> formData) {
        String agentName = formData.getOrDefault("agentName", "Agente");

        RentalRequestDTO dto = new RentalRequestDTO();
        String start = formData.get("startDate");
        if (start != null && !start.isBlank()) dto.setStartDate(LocalDate.parse(start));
        String end = formData.get("endDate");
        if (end != null && !end.isBlank()) dto.setEndDate(LocalDate.parse(end));
        dto.setNotes(formData.getOrDefault("notes", ""));

        try {
            rentalRequestService.agentModify(id, dto, agentName);
            return HttpResponse.redirect(URI.create("/agent/requests?success=Pedido+modificado+pelo+agente!"));
        } catch (BusinessRuleException ex) {
            Map<String, Object> model = new HashMap<>();
            model.put("requestForm", dto);
            model.put("requestId", id);
            model.put("request", rentalRequestService.findById(id));
            model.put("errorMessage", ex.getMessage());
            return new ModelAndView<>("agent/request-edit", model);
        }
    }
}

