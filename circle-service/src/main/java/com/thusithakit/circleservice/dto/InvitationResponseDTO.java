package com.thusithakit.circleservice.dto;

import com.thusithakit.circleservice.model.InvitationStatus;

import java.time.Instant;
import java.util.UUID;

public record InvitationResponseDTO(
    UUID id,
    UUID circleId,
    UUID invitedUserId,
    UUID invitedBy,
    InvitationStatus status,
    Instant expiresAt
) {
}
