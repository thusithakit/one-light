package com.thusithakit.deviceservice.repository;

import com.thusithakit.deviceservice.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

    List<Device> findAllByOwnerId(UUID ownerId);

    Optional<Device> findByIdAndOwnerId(
            UUID id,
            UUID ownerId
    );

    Optional<Device> findByDeviceIdentifier(
            String deviceIdentifier
    );

    boolean existsByDeviceIdentifier(
            String deviceIdentifier
    );
}
