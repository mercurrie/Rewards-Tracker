package com.javaproject.demo.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
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
                        // Public endpoints
                        .requestMatchers("/public/**", "/h2-console/**", "/api/auth/test").permitAll()
                        .requestMatchers("/api/transactions/**").permitAll()
                        // Development endpoints - you can secure these later
                        .requestMatchers("/api/**").permitAll()
                        // All other requests are permitted for now (can be changed to authenticated() later)
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
