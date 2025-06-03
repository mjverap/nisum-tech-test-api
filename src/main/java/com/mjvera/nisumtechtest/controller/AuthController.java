package com.mjvera.nisumtechtest.controller;

import com.mjvera.nisumtechtest.config.SecurityConfigProps;
import com.mjvera.nisumtechtest.dto.LoginRequestDTO;
import com.mjvera.nisumtechtest.dto.LoginResponseDTO;
import com.mjvera.nisumtechtest.service.AuthService;
import com.mjvera.nisumtechtest.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO user) {
        LoginResponseDTO response = authService.login(user);
        return ResponseEntity.ok(response);
    }
}
