package com.thusithakit.authservice.service;

import com.thusithakit.authservice.dto.LoginRequestDTO;
import com.thusithakit.authservice.dto.LoginResponseDTO;
import com.thusithakit.authservice.dto.RegisterRequestDTO;
import com.thusithakit.authservice.dto.RegisterResponseDTO;
import com.thusithakit.authservice.grpc.UserServiceGrpcClient;
import com.thusithakit.authservice.model.AuthUser;
import com.thusithakit.authservice.repository.AuthUserRepository;
import com.thusithakit.authservice.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserServiceGrpcClient userServiceGrpcClient;

    public AuthService(
            AuthUserRepository authUserRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            UserServiceGrpcClient userServiceGrpcClient
    ) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userServiceGrpcClient = userServiceGrpcClient;
    }

    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO request) {

        if (authUserRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException(
                    "Email already registered"
            );
        }

        AuthUser authUser = AuthUser.builder()
                .email(request.email().toLowerCase())
                .passwordHash(
                        passwordEncoder.encode(request.password())
                )
                .enabled(true)
                .build();

        authUser = authUserRepository.save(authUser);

        // Create corresponding profile in User Service
        var userProfile =
                userServiceGrpcClient.createUserProfile(
                        authUser.getId(),
                        request.name(),
                        request.timezone()
                );

        String token = jwtService.generateToken(
                authUser.getId(),
                authUser.getEmail()
        );

        return new RegisterResponseDTO(
                authUser.getId(),
                authUser.getEmail(),
                userProfile.getName(),
                token
        );
    }

    public LoginResponseDTO login(LoginRequestDTO request) {

        AuthUser authUser = authUserRepository
                .findByEmail(request.email().toLowerCase())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid email or password"
                        )
                );

        if (!authUser.isEnabled()) {
            throw new IllegalStateException(
                    "Account is disabled"
            );
        }

        if (!passwordEncoder.matches(
                request.password(),
                authUser.getPasswordHash()
        )) {
            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        String token = jwtService.generateToken(
                authUser.getId(),
                authUser.getEmail()
        );

        return new LoginResponseDTO(
                authUser.getId(),
                authUser.getEmail(),
                token
        );
    }
}
