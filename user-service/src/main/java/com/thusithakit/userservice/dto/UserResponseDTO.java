package com.thusithakit.userservice.dto;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(
    UUID id,
    UUID authUserId,
    String name,
    String avatarUrl,
    String timezone,
    Instant createdAt,
    Instant updatedAt
) {
}
