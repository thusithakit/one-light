package com.thusithakit.deviceservice.repository;

import com.thusithakit.deviceservice.model.LightSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LightSettingsRepository
    extends JpaRepository<LightSettings, UUID> {

  Optional<LightSettings> findByDevice_Id(UUID deviceId);
}
