package com.thusithakit.circleservice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record InviteMemberRequestDTO(
    @NotNull
    UUID userId
) {
}
