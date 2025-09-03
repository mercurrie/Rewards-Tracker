package com.javaproject.demo.controller;

import com.javaproject.demo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/user")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User u = (User) authentication.getPrincipal();
            return ResponseEntity.ok(Map.of(
                    "uid", u.getFirebaseUid(),
                    "email", u.getEmail(),
                    "displayName", u.getDisplayName(),
                    "role", u.getRole()
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Not authenticated"));
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User u = (User) authentication.getPrincipal();
            return ResponseEntity.ok("Authenticated as: " + u.getEmail() + " (Role: " + u.getRole() + ")");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
    }
}
