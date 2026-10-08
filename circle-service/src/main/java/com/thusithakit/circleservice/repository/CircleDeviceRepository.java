package com.thusithakit.circleservice.repository;

import com.thusithakit.circleservice.model.CircleDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CircleDeviceRepository
    extends JpaRepository<CircleDevice, UUID> {

  List<CircleDevice> findAllByCircleId(UUID circleId);

  List<CircleDevice> findAllByDeviceId(UUID deviceId);

  Optional<CircleDevice> findByCircleIdAndDeviceId(
      UUID circleId,
      UUID deviceId
  );

  boolean existsByCircleIdAndDeviceId(
      UUID circleId,
      UUID deviceId
  );

  void deleteByCircleIdAndDeviceId(
      UUID circleId,
      UUID deviceId
  );
}
