package com.thusithakit.deviceservice.dto;

import com.thusithakit.deviceservice.model.DeviceStatus;

import java.util.UUID;

public record PairDeviceResponseDTO(
        UUID deviceId,

        String deviceIdentifier,

        String name,

        DeviceStatus status
) {
}
