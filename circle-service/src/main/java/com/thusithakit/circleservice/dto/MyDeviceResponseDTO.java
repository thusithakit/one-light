package com.thusithakit.circleservice.dto;

import java.util.UUID;

public record MyDeviceResponseDTO(
    UUID deviceId,
    String name,
    String status,
    String firmwareVersion,
    String lastSeenAt,
    UUID circleId,
    String circleName,
    boolean canSendMessages
) {
}
