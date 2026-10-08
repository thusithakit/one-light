package com.thusithakit.circleservice.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateMemberPermissionRequest(
    @NotNull
    Boolean canSendMessages
) {
}
