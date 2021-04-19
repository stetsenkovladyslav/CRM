package com.cursor.onlineshop.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping(value = "/health")
    public ResponseEntity<String> createAuthenticationToken() {
        return ResponseEntity.ok("Hello world! I am alive!");
    }
}
