package com.viser.StockTrade.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtGenerator tokenGenerator;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Filters requests and performs JWT-based authentication.
     *
     * This method extracts a JWT token from the HTTP request, validates it, and if valid, retrieves
     * the username from the token and loads user details. It then creates an {@link UsernamePasswordAuthenticationToken}
     * with the user details and sets it in the {@link SecurityContextHolder}. This enables Spring Security to
     * handle authorization and authentication for the current request.
     *
     * @param request the {@link HttpServletRequest} object that contains the request the client made to the server
     * @param response the {@link HttpServletResponse} object that will be used to send a response back to the client
     * @param filterChain the {@link FilterChain} used to pass the request and response to the next filter in the chain
     * @throws ServletException if an error occurs during the filtering process
     * @throws IOException if an input or output error is detected
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromRequest(request);
        if (StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
            String username = tokenGenerator.getUsernameFromJWT(token);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the cookies in the HTTP request.
     *
     * This method iterates through the cookies of the given {@link HttpServletRequest} and looks
     * for a cookie named "jwtToken". If such a cookie is found, its value (the JWT token) is returned.
     * If no such cookie is present, the method returns {@code null}.
     *
     * @param request the {@link HttpServletRequest} object that contains the request the client made to the server
     * @return the JWT token as a {@link String} if the "jwtToken" cookie is found; {@code null} otherwise
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
