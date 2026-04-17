package com.pucminas.car_rental_system.controller;

import com.pucminas.car_rental_system.domain.dto.CarDTO;
import com.pucminas.car_rental_system.domain.dto.CreditContractDTO;
import com.pucminas.car_rental_system.domain.dto.ExecuteContractDTO;
import com.pucminas.car_rental_system.domain.entity.CreditContract;
import com.pucminas.car_rental_system.domain.entity.RentalContract;
import com.pucminas.car_rental_system.exception.BusinessRuleException;
import com.pucminas.car_rental_system.service.CreditContractService;
import com.pucminas.car_rental_system.service.RentalContractService;
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

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller para contratos de aluguel e crédito (Histórias 8-9).
 */
@Controller("/contracts")
public class ContractController {

    private final RentalContractService rentalContractService;
    private final CreditContractService creditContractService;
    private final RentalRequestService rentalRequestService;

    public ContractController(RentalContractService rentalContractService,
                               CreditContractService creditContractService,
                               RentalRequestService rentalRequestService) {
        this.rentalContractService = rentalContractService;
        this.creditContractService = creditContractService;
        this.rentalRequestService = rentalRequestService;
    }

    // ═══ EXECUTE RENTAL CONTRACT (História 9) ═══

    @Get("/execute/{requestId}")
    @View("contract/execute-form")
    public Map<String, Object> showExecuteForm(@PathVariable Long requestId) {
        Map<String, Object> model = new HashMap<>();
        model.put("request", rentalRequestService.findById(requestId));
        model.put("requestId", requestId);
        model.put("contractForm", new ExecuteContractDTO());
        return model;
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/execute")
    public Object executeContract(@Body Map<String, String> formData) {
        ExecuteContractDTO dto = buildExecuteContractDTO(formData);

        try {
            rentalContractService.executeContract(dto);
            return HttpResponse.redirect(URI.create("/agent/requests?success=Contrato+executado+com+sucesso!"));
        } catch (BusinessRuleException ex) {
            Map<String, Object> model = new HashMap<>();
            model.put("request", rentalRequestService.findById(dto.getRentalRequestId()));
            model.put("requestId", dto.getRentalRequestId());
            model.put("contractForm", dto);
            model.put("errorMessage", ex.getMessage());
            return new ModelAndView<>("contract/execute-form", model);
        }
    }

    // ═══ ISSUE CREDIT CONTRACT (História 8) ═══

    @Get("/credit/{requestId}")
    @View("contract/credit-form")
    public Map<String, Object> showCreditForm(@PathVariable Long requestId) {
        Map<String, Object> model = new HashMap<>();
        model.put("request", rentalRequestService.findById(requestId));
        model.put("requestId", requestId);
        model.put("creditForm", new CreditContractDTO());
        return model;
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/credit")
    public Object issueCredit(@Body Map<String, String> formData) {
        CreditContractDTO dto = buildCreditContractDTO(formData);

        try {
            creditContractService.issueCredit(dto);
            return HttpResponse.redirect(URI.create("/agent/requests?success=Crédito+concedido+com+sucesso!"));
        } catch (BusinessRuleException ex) {
            Map<String, Object> model = new HashMap<>();
            model.put("request", rentalRequestService.findById(dto.getRentalRequestId()));
            model.put("requestId", dto.getRentalRequestId());
            model.put("creditForm", dto);
            model.put("errorMessage", ex.getMessage());
            return new ModelAndView<>("contract/credit-form", model);
        }
    }

    // ═══ VIEW CONTRACT ═══

    @Get("/{id}")
    @View("contract/detail")
    public Map<String, Object> contractDetail(@PathVariable Long id) {
        Map<String, Object> model = new HashMap<>();
        RentalContract contract = rentalContractService.findById(id);
        model.put("contract", contract);

        CreditContract credit = creditContractService.findByRequestId(
                contract.getRentalRequest().getId());
        model.put("credit", credit);
        return model;
    }

    // ═══ HELPERS ═══

    private ExecuteContractDTO buildExecuteContractDTO(Map<String, String> form) {
        CarDTO car = CarDTO.builder()
                .registration(form.getOrDefault("car.registration", ""))
                .year(parseInteger(form.get("car.year")))
                .brand(form.getOrDefault("car.brand", ""))
                .model(form.getOrDefault("car.model", ""))
                .plate(form.getOrDefault("car.plate", ""))
                .build();

        return ExecuteContractDTO.builder()
                .rentalRequestId(parseLong(form.get("rentalRequestId")))
                .car(car)
                .totalValue(parseBigDecimal(form.get("totalValue")))
                .carOwnerType(form.getOrDefault("carOwnerType", "CLIENT"))
                .carOwnerName(form.getOrDefault("carOwnerName", ""))
                .build();
    }

    private CreditContractDTO buildCreditContractDTO(Map<String, String> form) {
        return CreditContractDTO.builder()
                .rentalRequestId(parseLong(form.get("rentalRequestId")))
                .bankName(form.getOrDefault("bankName", ""))
                .creditValue(parseBigDecimal(form.get("creditValue")))
                .interestRate(parseBigDecimal(form.get("interestRate")))
                .build();
    }

    private Long parseLong(String s) {
        try { return s != null ? Long.parseLong(s) : null; } catch (NumberFormatException e) { return null; }
    }

    private Integer parseInteger(String s) {
        try { return s != null ? Integer.parseInt(s) : null; } catch (NumberFormatException e) { return null; }
    }

    private BigDecimal parseBigDecimal(String s) {
        try { return s != null && !s.isBlank() ? new BigDecimal(s) : null; } catch (NumberFormatException e) { return null; }
    }
}

