package com.thusithakit.circleservice.dto;

import java.time.Instant;
import java.util.UUID;

public record CircleDeviceResponseDTO(
    UUID deviceId,
    Instant addedAt
) {
}
