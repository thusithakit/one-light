package com.thusithakit.messageservice.dto;

import com.thusithakit.messageservice.model.MessageStatus;

import java.time.Instant;
import java.util.UUID;

public record MessageResponseDTO(
    UUID id,

    UUID deviceId,

    UUID senderUserId,

    String content,

    MessageStatus status,

    Instant createdAt,

    Instant deliveredAt
) {
}
