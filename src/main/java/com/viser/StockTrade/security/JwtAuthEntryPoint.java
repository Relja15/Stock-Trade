package com.viser.StockTrade.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    //TODO ukoliko je korisnik autentifikovan, a nema dozvole za pristup izbaciti mu poruku
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String requestedUri = request.getRequestURI();
        if (!requestedUri.startsWith("/api/")) {
            response.sendRedirect("/login-page");
        }
    }
}
