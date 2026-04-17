package com.pucminas.car_rental_system.exception;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.views.View;

import java.util.Map;

/**
 * Tratamento centralizado de exceções — padrão {@code @Error} do Micronaut.
 *
 * <p>Intercepta exceções globais e as converte em páginas de erro
 * renderizadas via Thymeleaf, seguindo o RFC 7807 em espírito.</p>
 */
@Controller
public class GlobalExceptionHandler {

    @Error(exception = ResourceNotFoundException.class, global = true)
    @View("error/404")
    public Map<String, Object> handleResourceNotFound(HttpRequest<?> request, ResourceNotFoundException ex) {
        return Map.of("message", ex.getMessage());
    }

    @Error(exception = BusinessRuleException.class, global = true)
    @View("error/422")
    public Map<String, Object> handleBusinessRule(HttpRequest<?> request, BusinessRuleException ex) {
        return Map.of("message", ex.getMessage());
    }

    @Error(status = HttpStatus.NOT_FOUND, global = true)
    @View("error/404")
    public Map<String, Object> handleNotFound(HttpRequest<?> request) {
        return Map.of("message", "O recurso solicitado não foi encontrado.");
    }

    @Error(status = HttpStatus.INTERNAL_SERVER_ERROR, global = true)
    @View("error/500")
    public Map<String, Object> handleServerError(HttpRequest<?> request) {
        return Map.of("message", "Ocorreu um erro interno. Tente novamente mais tarde.");
    }
}
