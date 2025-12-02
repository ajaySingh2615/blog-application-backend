package com.cadt.blogapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // Allows @PreAuthorize annotations
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // 1. Password Encoder (BCrypt)
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Authentication Manager (Manages Login)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // 3. The Security Filter Chain (The Rules)
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF for REST APIs
                .authorizeHttpRequests((authorize) ->
                        // authorize.anyRequest().authenticated() // (Lock everything - old way)

                        authorize.requestMatchers(HttpMethod.GET, "/api/**").permitAll() // Allow GET to everyone
                                .requestMatchers("/api/auth/**").permitAll() // Allow Login/Register to everyone
                                .anyRequest().authenticated() // Lock everything else
                ).httpBasic(Customizer.withDefaults()); // Use Basic Auth for now (We will swap to JWT later)

        return http.build();
    }

}