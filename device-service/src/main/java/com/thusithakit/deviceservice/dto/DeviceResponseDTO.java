package com.thusithakit.deviceservice.dto;

import com.thusithakit.deviceservice.model.DeviceStatus;

import java.time.Instant;
import java.util.UUID;

public record DeviceResponseDTO(
        UUID id,

        String deviceIdentifier,

        String name,

        DeviceStatus status,

        String firmwareVersion,

        Instant lastSeenAt,

        Instant createdAt,

        Instant updatedAt
) {
}
