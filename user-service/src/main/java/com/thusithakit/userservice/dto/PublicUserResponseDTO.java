package com.thusithakit.userservice.dto;

import java.time.Instant;
import java.util.UUID;

public record PublicUserResponseDTO(
    UUID id,
    String name,
    String avatarUrl,
    String timezone,
    Instant createdAt,
    Instant updatedAt
) {
}
