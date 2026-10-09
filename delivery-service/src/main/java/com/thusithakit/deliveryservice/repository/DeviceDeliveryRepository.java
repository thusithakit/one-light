package com.thusithakit.deliveryservice.repository;

import com.thusithakit.deliveryservice.model.DeviceDelivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeviceDeliveryRepository
    extends JpaRepository<DeviceDelivery, UUID> {

  Optional<DeviceDelivery> findByMessageId(String messageId);
}
