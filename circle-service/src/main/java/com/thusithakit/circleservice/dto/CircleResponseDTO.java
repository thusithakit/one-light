package com.thusithakit.circleservice.dto;

import java.time.Instant;
import java.util.UUID;

public record CircleResponseDTO(
    UUID id,
    String name,
    UUID ownerId,
    Instant createdAt,
    Instant updatedAt
) {
}
