package com.thusithakit.authservice.dto;

public record RefreshResponseDTO(
    String accessToken,
    String refreshToken,
    long refreshTokenExpiresIn
) {
}
