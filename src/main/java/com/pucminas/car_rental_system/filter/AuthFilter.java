package com.pucminas.car_rental_system.filter;

import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.cookie.Cookie;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import org.reactivestreams.Publisher;

import java.net.URI;
import java.util.Set;

/**
 * Filtro HTTP que protege rotas exigindo autenticação via cookie USER_ID.
 * Rotas públicas (login, assets estáticos) são liberadas.
 */
@Filter("/**")
public class AuthFilter implements HttpServerFilter {

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/login", "/logout", "/static"
    );

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        String path = request.getPath();

        // Permite rotas públicas e assets estáticos
        if (isPublic(path)) {
            return chain.proceed(request);
        }

        // Verifica cookie de autenticação
        String userId = request.getCookies().findCookie("USER_ID")
                .map(Cookie::getValue)
                .orElse(null);

        if (userId == null || userId.isBlank()) {
            return Publishers.just(HttpResponse.redirect(URI.create("/login")));
        }

        return chain.proceed(request);
    }

    private boolean isPublic(String path) {
        for (String publicPath : PUBLIC_PATHS) {
            if (path.equals(publicPath) || path.startsWith(publicPath + "/") || path.startsWith(publicPath + "?")) {
                return true;
            }
        }
        return false;
    }
}
