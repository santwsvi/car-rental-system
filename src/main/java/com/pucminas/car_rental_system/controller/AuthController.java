package com.pucminas.car_rental_system.controller;

import com.pucminas.car_rental_system.domain.entity.User;
import com.pucminas.car_rental_system.service.AuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.cookie.Cookie;
import io.micronaut.views.View;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controller de autenticação — login e logout via cookie de sessão.
 */
@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Get("/login")
    @View("auth/login")
    public Map<String, Object> loginPage() {
        return new HashMap<>();
    }

    @Post("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public HttpResponse<?> login(@Body Map<String, String> form) {
        String email = form.get("email");
        String password = form.get("password");

        Optional<User> user = authService.authenticate(email, password);

        if (user.isPresent()) {
            User u = user.get();
            Cookie cookie = Cookie.of("USER_ID", String.valueOf(u.getId()))
                    .path("/")
                    .httpOnly(true);
            Cookie roleCookie = Cookie.of("USER_ROLE", u.getRole().name())
                    .path("/")
                    .httpOnly(true);
            Cookie nameCookie = Cookie.of("USER_NAME", u.getDisplayName())
                    .path("/")
                    .httpOnly(false);

            // Redireciona com base no role
            String redirectUrl = switch (u.getRole()) {
                case CLIENT -> "/rental-requests";
                case AGENT_COMPANY, AGENT_BANK -> "/agent/requests";
            };

            return HttpResponse.redirect(URI.create(redirectUrl))
                    .cookie(cookie)
                    .cookie(roleCookie)
                    .cookie(nameCookie);
        }

        // Credenciais inválidas — volta ao login com erro
        return HttpResponse.redirect(URI.create("/login?error=true"));
    }

    @Get("/logout")
    public HttpResponse<?> logout() {
        Cookie userIdCookie = Cookie.of("USER_ID", "").path("/").maxAge(0);
        Cookie roleCookie = Cookie.of("USER_ROLE", "").path("/").maxAge(0);
        Cookie nameCookie = Cookie.of("USER_NAME", "").path("/").maxAge(0);

        return HttpResponse.redirect(URI.create("/login"))
                .cookie(userIdCookie)
                .cookie(roleCookie)
                .cookie(nameCookie);
    }
}


