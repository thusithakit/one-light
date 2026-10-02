package com.thusithakit.authservice.dto;

import java.util.UUID;

public record LoginResponseDTO(
    UUID userId,
    String email,
    String accessToken
) {
}
