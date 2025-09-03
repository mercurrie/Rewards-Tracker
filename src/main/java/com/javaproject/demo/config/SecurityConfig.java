package com.javaproject.demo.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final ObjectProvider<com.javaproject.demo.security.FirebaseAuthenticationFilter> firebaseAuthenticationFilter;

    public SecurityConfig(ObjectProvider<com.javaproject.demo.security.FirebaseAuthenticationFilter> firebaseAuthenticationFilter) {
        this.firebaseAuthenticationFilter = firebaseAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints and static UI
                        .requestMatchers("/public/**", "/h2-console/**", "/auth/**", "/static/**", "/favicon.ico").permitAll()
                        // Auth endpoints (sign-in related) are public
                        .requestMatchers("/api/auth/**").permitAll()
                        // Require authentication for other API endpoints
                        .requestMatchers("/api/**").authenticated()
                        // All other requests (resources) permitted
                        .anyRequest().permitAll());
        
        // Add Firebase filter if available
        com.javaproject.demo.security.FirebaseAuthenticationFilter filter = firebaseAuthenticationFilter.getIfAvailable();
        if (filter != null) {
            System.out.println("Firebase authentication filter is available and configured");
            http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        } else {
            System.out.println("Firebase authentication filter is not available - check Firebase configuration");
        }
        
        // Allow H2 console frames
        http.headers(h -> h.frameOptions(f -> f.sameOrigin()));
        return http.build();
    }
}
