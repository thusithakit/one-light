package com.thusithakit.authservice.dto;

import java.util.UUID;

public record RegisterResponseDTO(
    UUID userId,
    String email,
    String name,
    String accessToken
) {
}
