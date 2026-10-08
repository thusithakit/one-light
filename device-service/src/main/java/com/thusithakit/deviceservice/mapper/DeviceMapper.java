package com.thusithakit.deviceservice.mapper;

import com.thusithakit.deviceservice.dto.DeviceResponseDTO;
import com.thusithakit.deviceservice.model.Device;
import com.thusithakit.deviceservice.model.DeviceStatus;

import java.time.Instant;
import java.util.UUID;

public class DeviceMapper {
    public static DeviceResponseDTO toResponse(Device device){
        return new DeviceResponseDTO(
                device.getId(),
                device.getDeviceIdentifier(),
                device.getName(),
                device.getStatus(),
                device.getFirmwareVersion(),
                device.getLastSeenAt(),
                device.getCreatedAt(),
                device.getUpdatedAt()
        );
    }
}
