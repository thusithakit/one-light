package com.thusithakit.messageservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record SendMessageRequestDTO(
    @NotNull
    UUID deviceId,

    @NotBlank
    @Size(max = 500)
    String content
) {
}
