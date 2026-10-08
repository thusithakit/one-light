package com.thusithakit.deviceservice.dto;

import java.time.Instant;
import java.util.UUID;

public record CreatePairingSessionResponseDTO(
        UUID deviceId,
        String pairingCode,
        Instant expiresAt
) {
}
