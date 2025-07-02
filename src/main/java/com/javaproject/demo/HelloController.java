package com.javaproject.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Hello from Spring Boot!");
    }

    @PostMapping("/hello")
    public Map<String, String> createHello(@RequestBody Map<String, String> request) {
        String name = request.getOrDefault("name", "World");
        return Map.of(
            "message", "Hello " + name + "!",
            "status", "created",
            "timestamp", java.time.Instant.now().toString()
        );
    }
}
