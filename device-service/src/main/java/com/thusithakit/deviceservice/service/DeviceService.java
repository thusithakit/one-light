package com.thusithakit.deviceservice.service;

import com.thusithakit.deviceservice.dto.DeviceResponseDTO;
import com.thusithakit.deviceservice.dto.UpdateDeviceRequestDTO;
import com.thusithakit.deviceservice.mapper.DeviceMapper;
import com.thusithakit.deviceservice.model.Device;
import com.thusithakit.deviceservice.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository){
        this.deviceRepository = deviceRepository;
    }

    public List<DeviceResponseDTO> getAllDevices(UUID ownerId){
        List<Device> devices = deviceRepository
                .findAllByOwnerId(ownerId);

        return devices.stream().map(DeviceMapper::toResponse).toList();
    }

    public DeviceResponseDTO getDevice(UUID ownerId, UUID deviceId) {
        Device device = deviceRepository.findByIdAndOwnerId(deviceId,ownerId)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        return DeviceMapper.toResponse(device);
    }

    public DeviceResponseDTO updateDevice(UUID ownerId, UUID deviceId, UpdateDeviceRequestDTO request){
        Device device = deviceRepository.findByIdAndOwnerId(deviceId,ownerId)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        if (request.name() != null) {
            device.setName(request.name());
        }

        Device updatedDevice = deviceRepository.save(device);

        return DeviceMapper.toResponse(updatedDevice);
    }

    public void deleteDevice(UUID ownerId, UUID deviceId){
        Device device = deviceRepository.findByIdAndOwnerId(deviceId,ownerId)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        deviceRepository.delete(device);
    }
}
