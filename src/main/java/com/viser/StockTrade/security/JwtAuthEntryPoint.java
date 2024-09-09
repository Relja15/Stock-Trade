package com.viser.StockTrade.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * Handles authentication exceptions by redirecting the user to the login page if the request URI does not start with "/api/".
     *
     * This method is invoked when an authentication exception is thrown. If the request URI does not start with "/api/",
     * the user is redirected to the "/login-page". This is typically used to redirect unauthenticated users to the login page
     * when they try to access protected resources.
     *
     * @param request the {@link HttpServletRequest} object that contains the request the client made to the server
     * @param response the {@link HttpServletResponse} object that contains the response the server sends to the client
     * @param authException the {@link AuthenticationException} that was thrown during the authentication process
     * @throws IOException if an input or output exception occurs
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String requestedUri = request.getRequestURI();
        if (!requestedUri.startsWith("/api/")) {
            response.sendRedirect("/login-page");
        }
    }
}
