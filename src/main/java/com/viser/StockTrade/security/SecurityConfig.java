package com.viser.StockTrade.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthEntryPoint authEntryPoint;

    @Autowired
    public SecurityConfig(JwtAuthEntryPoint authEntryPoint) {
        this.authEntryPoint = authEntryPoint;
    }

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * This method sets up security configurations for handling HTTP requests in the application. It disables CSRF protection,
     * specifies the handling of authentication exceptions, sets the session management policy to stateless, and configures
     * authorization rules for various request patterns. The method also adds a JWT authentication filter to the chain.
     *
     * @param http the {@link HttpSecurity} object to configure security settings
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during the configuration of the security filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authEntryPoint))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(SecurityConstants.authWhiteList).permitAll()
                        .requestMatchers(SecurityConstants.adminRoot).hasAuthority("ADMIN")
                        .requestMatchers(SecurityConstants.userRoot).hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Provides an {@link AuthenticationManager} bean for authentication.
     *
     * This method retrieves and returns an instance of {@link AuthenticationManager} from the provided
     * {@link AuthenticationConfiguration}. It is used to handle authentication requests and validate user credentials.
     *
     * @param authenticationConfiguration the {@link AuthenticationConfiguration} used to obtain the {@link AuthenticationManager}
     * @return the configured {@link AuthenticationManager}
     * @throws Exception if an error occurs while retrieving the {@link AuthenticationManager}
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Provides a {@link PasswordEncoder} bean for encoding passwords.
     *
     * This method returns an instance of {@link BCryptPasswordEncoder}, which is used to encode user passwords
     * for secure storage. BCrypt is a widely used hashing function designed to be slow to mitigate brute-force
     * attacks and includes a salt to enhance security.
     *
     * @return a {@link PasswordEncoder} instance configured with {@link BCryptPasswordEncoder}
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides a {@link JwtAuthenticationFilter} bean for JWT authentication.
     *
     * This method creates and returns an instance of {@link JwtAuthenticationFilter}, which is responsible
     * for processing JWT tokens in HTTP requests. The filter checks the presence and validity of the JWT token,
     * extracts user information, and sets the authentication context if the token is valid. This filter should
     * be added to the security filter chain to handle JWT-based authentication.
     *
     * @return a {@link JwtAuthenticationFilter} instance
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
