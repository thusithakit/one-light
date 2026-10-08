package com.thusithakit.deviceservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePairingSessionRequestDTO(
    @NotBlank
    @Size(max = 100)
    String deviceIdentifier,

    @Size(max = 50)
    String firmwareVersion
) {
}
