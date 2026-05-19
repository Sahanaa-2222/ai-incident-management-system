package com.finshield.incident_management.controller;

import com.finshield.incident_management.dto.LoginRequest;
import com.finshield.incident_management.dto.LoginResponse;
import com.finshield.incident_management.model.User;
import com.finshield.incident_management.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = authService.authenticate(request.getUsername(), request.getPassword());
        if (user != null) {
            String token = "simple-token-" + user.getId();
            LoginResponse response = new LoginResponse(token, user.getUsername(), user.getRole().toString());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}