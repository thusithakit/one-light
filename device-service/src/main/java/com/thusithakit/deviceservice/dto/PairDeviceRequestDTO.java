package com.thusithakit.deviceservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PairDeviceRequestDTO(
    @NotBlank
    @Size(min = 6, max = 6)
    String pairingCode
) {
}
