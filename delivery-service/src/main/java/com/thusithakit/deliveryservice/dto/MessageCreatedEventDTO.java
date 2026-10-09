package com.thusithakit.deliveryservice.dto;

import java.time.Instant;

public record MessageCreatedEventDTO(
    String messageId,
    String deviceId,
    String senderUserId,
    String content,
    Instant createdAt
) {
}
