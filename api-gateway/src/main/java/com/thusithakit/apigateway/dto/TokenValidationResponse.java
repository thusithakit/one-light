package com.thusithakit.apigateway.dto;

import java.util.UUID;

public record TokenValidationResponse(
        boolean valid,
        UUID userId,
        String email
) {
}
