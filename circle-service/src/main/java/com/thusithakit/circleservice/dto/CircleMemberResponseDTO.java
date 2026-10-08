package com.thusithakit.circleservice.dto;

import com.thusithakit.circleservice.model.CircleMemberRole;

import java.time.Instant;
import java.util.UUID;

public record CircleMemberResponseDTO(
    UUID userId,
    CircleMemberRole role,
    boolean canSendMessages,
    Instant joinedAt
) {
}
