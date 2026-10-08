package com.thusithakit.circleservice.dto;

import com.thusithakit.circleservice.model.CircleMemberRole;

import java.time.Instant;
import java.util.UUID;

public record MyCircleResponseDTO(
    UUID circleId,
    String name,
    UUID ownerId,
    CircleMemberRole role,
    boolean canSendMessages,
    Instant joinedAt
) {
}
