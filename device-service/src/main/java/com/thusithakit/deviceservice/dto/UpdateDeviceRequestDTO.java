package com.thusithakit.deviceservice.dto;

import jakarta.validation.constraints.Size;

public record UpdateDeviceRequestDTO(
    @Size(max = 100)
    String name
) {
}
