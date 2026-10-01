package com.thusithakit.userservice.dto;

import jakarta.validation.constraints.Size;

public record UpdateUserRequestDTO(
    @Size(max = 100)
    String name,

    @Size(max = 500)
    String avatarUrl,

    @Size(max = 100)
    String timezone
) {
}
