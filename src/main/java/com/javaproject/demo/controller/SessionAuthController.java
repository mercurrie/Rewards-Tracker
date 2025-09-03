package com.javaproject.demo.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.javaproject.demo.model.User;
import com.javaproject.demo.service.FirebaseAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/auth")
public class SessionAuthController {

    private final FirebaseAuthService authService;

    public SessionAuthController(FirebaseAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/session")
    public ResponseEntity<?> createSession(@RequestBody Map<String, String> body, HttpServletResponse response) {
        try {
            String idToken = body.get("idToken");
            if (idToken == null) return ResponseEntity.badRequest().body(Map.of("error", "missing idToken"));

            long expiresIn = TimeUnit.DAYS.toMillis(5);
            String sessionCookie = authService.createSessionCookie(idToken, expiresIn);

            Cookie cookie = new Cookie("session", sessionCookie);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // set true in prod with HTTPS
            cookie.setPath("/");
            cookie.setMaxAge((int) (expiresIn / 1000));
            response.addCookie(cookie);

            // Optional: verify and touch local user (ensure local DB user is created/updated)
            authService.verify(idToken);

            return ResponseEntity.ok(Map.of("status", "ok"));
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid idToken", "message", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {
        try {
            String sessionCookie = null;
            if (request.getCookies() != null) {
                for (Cookie c : request.getCookies()) {
                    if ("session".equals(c.getName())) {
                        sessionCookie = c.getValue();
                        break;
                    }
                }
            }

            if (sessionCookie != null) {
                try {
                    User u = authService.verifySessionCookie(sessionCookie);
                    return ResponseEntity.ok(Map.of(
                            "uid", u.getFirebaseUid(),
                            "email", u.getEmail(),
                            "displayName", u.getDisplayName(),
                            "role", u.getRole()
                    ));
                } catch (FirebaseAuthException e) {
                    // fallthrough to try Authorization header
                }
            }

            // Fallback: check Authorization header
            String auth = request.getHeader("Authorization");
            if (auth != null && auth.startsWith("Bearer ")) {
                String idToken = auth.substring(7);
                User u = authService.verify(idToken);
                return ResponseEntity.ok(Map.of(
                        "uid", u.getFirebaseUid(),
                        "email", u.getEmail(),
                        "displayName", u.getDisplayName(),
                        "role", u.getRole()
                ));
            }

            return ResponseEntity.status(401).body(Map.of("error", "not authenticated"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid session", "message", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("session", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true in prod
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok(Map.of("status", "logged_out"));
    }
}
