package com.thusithakit.authservice.controller;

import com.thusithakit.authservice.dto.*;
import com.thusithakit.authservice.service.AuthService;
import com.thusithakit.authservice.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final boolean secureCookie;
    private final long maxTtl;

    public AuthController(
        AuthService authService,
        RefreshTokenService refreshTokenService,
        @Value("${auth.cookie-secure:false}") boolean secureCookie,
        @Value("${auth.refresh-token-ttl-seconds:604800}") long maxTtl)
    {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.secureCookie = secureCookie;
        this.maxTtl = maxTtl;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(
        @Valid @RequestBody RegisterRequestDTO request
    ) {
        RegisterResponseDTO registerResponse = authService.register(request);

        String refreshToken =
            refreshTokenService.createRefreshToken(registerResponse.userId());

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(
                HttpHeaders.SET_COOKIE,
                refreshCookie(refreshToken, maxTtl).toString()
            )
            .header(HttpHeaders.CACHE_CONTROL, "no-store")
            .body(registerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        LoginResponseDTO loginResponse = authService.login(request);

        String refreshToken =
            refreshTokenService.createRefreshToken(loginResponse.userId());

        ResponseCookie cookie = refreshCookie(refreshToken, maxTtl);

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .header(HttpHeaders.CACHE_CONTROL, "no-store")
            .body(loginResponse);
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(
            @RequestHeader("Authorization") String authorization
    ) {

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenValidationResponse(false, null, null));
        }

        String token = authorization.substring(7);

        TokenValidationResponse response =
                authService.validateToken(token);

        if (!response.valid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        @CookieValue(
            name = "refresh_token",
            required = false
        ) String refreshToken,
        HttpServletResponse response
    ) {
        refreshTokenService.logout(refreshToken);

        response.addHeader(
            HttpHeaders.SET_COOKIE,
            refreshCookie("", 0).toString()
        );

        return ResponseEntity.noContent()
            .header(HttpHeaders.CACHE_CONTROL, "no-store")
            .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponseDTO> refresh(
        @CookieValue(
            name = "refresh_token",
            required = false
        ) String refreshToken,
        HttpServletResponse response
    ) {
        RefreshResponseDTO pair = refreshTokenService.refresh(refreshToken);

        response.addHeader(
            HttpHeaders.SET_COOKIE,
            refreshCookie(
                pair.refreshToken(),
                pair.refreshTokenExpiresIn()
            ).toString()
        );

        return ResponseEntity.ok()
            .header(HttpHeaders.CACHE_CONTROL, "no-store")
            .body(new AccessTokenResponseDTO(pair.accessToken()));
    }

    private ResponseCookie refreshCookie(
        String value,
        long maxAgeSeconds
    ) {
        return ResponseCookie.from("refresh_token", value)
            .httpOnly(true)
            .secure(secureCookie)
            .sameSite("Lax")
            .path("/api/v1/auth")
            .maxAge(Duration.ofSeconds(maxAgeSeconds))
            .build();
    }

    private ResponseCookie buildRefreshCookie(
        String token,
        long maxAgeSeconds
    ) {
        return ResponseCookie.from("refresh_token", token)
            .httpOnly(true)
            .secure(secureCookie)
            .sameSite("None")
            .path("/api/v1/auth")
            .maxAge(Duration.ofSeconds(maxAgeSeconds))
            .build();
    }
}
