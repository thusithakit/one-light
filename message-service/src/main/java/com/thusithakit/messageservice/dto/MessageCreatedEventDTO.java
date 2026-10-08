package com.thusithakit.messageservice.dto;

import java.time.Instant;
import java.util.UUID;

public record MessageCreatedEventDTO(
    UUID messageId,

    UUID deviceId,

    UUID senderUserId,

    String content,

    Instant createdAt
) {
}
