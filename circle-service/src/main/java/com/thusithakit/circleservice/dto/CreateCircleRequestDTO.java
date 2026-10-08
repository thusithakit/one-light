package com.thusithakit.circleservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCircleRequestDTO(
    @NotBlank
    @Size(max = 100)
    String name
) {
}
