package com.thusithakit.authservice.controller;

import com.thusithakit.authservice.dto.LoginRequestDTO;
import com.thusithakit.authservice.dto.LoginResponseDTO;
import com.thusithakit.authservice.dto.RegisterRequestDTO;
import com.thusithakit.authservice.dto.RegisterResponseDTO;
import com.thusithakit.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponseDTO register(
            @Valid @RequestBody RegisterRequestDTO request
    ) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        return authService.login(request);
    }
}
