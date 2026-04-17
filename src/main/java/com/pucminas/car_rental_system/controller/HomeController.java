package com.pucminas.car_rental_system.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.net.URI;

/**
 * Controller para a página inicial do sistema.
 */
@Controller
public class HomeController {

    @Get("/")
    public HttpResponse<?> home() {
        return HttpResponse.redirect(URI.create("/clients"));
    }
}
