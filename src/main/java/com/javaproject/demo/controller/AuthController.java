package com.javaproject.demo.controller;

import com.javaproject.demo.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/me")
    public User getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    @GetMapping("/test")
    public String test(Authentication authentication) {
        if (authentication != null) {
            User user = (User) authentication.getPrincipal();
            return "Authenticated as: " + user.getEmail() + " (Role: " + user.getRole() + ")";
        }
        return "Not authenticated";
    }
}
