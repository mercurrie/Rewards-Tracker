package com.javaproject.demo.config;

import com.google.firebase.auth.FirebaseAuth;
import com.javaproject.demo.service.FirebaseAuthService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestFirebaseConfig {

    @Bean
    @Primary
    public FirebaseAuth mockFirebaseAuth() {
        return Mockito.mock(FirebaseAuth.class);
    }

    @Bean
    @Primary
    public FirebaseAuthService mockFirebaseAuthService() {
        return Mockito.mock(FirebaseAuthService.class);
    }
}
