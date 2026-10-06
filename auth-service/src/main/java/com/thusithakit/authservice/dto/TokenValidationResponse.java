package com.thusithakit.authservice.dto;

import java.util.UUID;

public record TokenValidationResponse(
        boolean valid,
        UUID userId,
        String email
) {
}
