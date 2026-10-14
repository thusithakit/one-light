package com.thusithakit.deviceservice.dto;

import com.thusithakit.deviceservice.model.LightSettings;

import java.time.Instant;
import java.util.UUID;

public record LightSettingsResponseDTO(
    UUID deviceId,
    boolean on,
    int brightness,
    String color,
    Instant updatedAt
) {
  public static LightSettingsResponseDTO from(
      LightSettings settings
  ) {
    return new LightSettingsResponseDTO(
        settings.getDevice().getId(),
        settings.isOn(),
        settings.getBrightness(),
        settings.getColor(),
        settings.getUpdatedAt()
    );
  }
}
