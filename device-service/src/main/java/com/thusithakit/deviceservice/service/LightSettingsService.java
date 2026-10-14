package com.thusithakit.deviceservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thusithakit.deviceservice.dto.LightSettingsRequestDTO;
import com.thusithakit.deviceservice.dto.LightSettingsResponseDTO;
import com.thusithakit.deviceservice.model.Device;
import com.thusithakit.deviceservice.model.LightSettings;
import com.thusithakit.deviceservice.mqtt.MqttPublisher;
import com.thusithakit.deviceservice.repository.DeviceRepository;
import com.thusithakit.deviceservice.repository.LightSettingsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class LightSettingsService {

  private final DeviceRepository deviceRepository;
  private final LightSettingsRepository settingsRepository;
  private final MqttPublisher mqttPublisher;
  private final ObjectMapper objectMapper;

  public LightSettingsService(
      DeviceRepository deviceRepository,
      LightSettingsRepository settingsRepository,
      MqttPublisher mqttPublisher,
      ObjectMapper objectMapper
  ) {
    this.deviceRepository = deviceRepository;
    this.settingsRepository = settingsRepository;
    this.mqttPublisher = mqttPublisher;
    this.objectMapper = objectMapper;
  }

  @Transactional(readOnly = true)
  public LightSettingsResponseDTO get(UUID deviceId) {
    Device device = deviceRepository.findById(deviceId)
        .orElseThrow(() ->
            new EntityNotFoundException("Device not found"));

    LightSettings settings = settingsRepository
        .findByDevice_Id(deviceId)
        .orElseGet(() -> new LightSettings(device));

    return LightSettingsResponseDTO.from(settings);
  }

  @Transactional
  public UpdateResult update(
      UUID ownerId,
      UUID deviceId,
      LightSettingsRequestDTO request
  ) {
    if (request == null || request.isEmpty()) {
      throw new IllegalArgumentException(
          "At least one setting is required"
      );
    }

    Device device = deviceRepository.findByIdAndOwnerId(deviceId,ownerId)
        .orElseThrow(() ->
            new EntityNotFoundException("Device not found"));

    LightSettings settings = settingsRepository
        .findByDevice_Id(deviceId)
        .orElseGet(() -> new LightSettings(device));

    if (request.on() != null) {
      settings.setOn(request.on());
    }

    if (request.brightness() != null) {
      settings.setBrightness(request.brightness());
    }

    if (request.color() != null) {
      settings.setColor(request.color());
    }

    settings = settingsRepository.saveAndFlush(settings);

    // Match the JSON contract expected by the ESP32 firmware.
    Map<String, Object> light = new LinkedHashMap<>();
    light.put("on", settings.isOn());
    light.put("brightness", settings.getBrightness());
    light.put("color", settings.getColor());

    Map<String, Object> payload = Map.of(
        "action", "set_light",
        "light", light
    );

    try {
      String json = objectMapper.writeValueAsString(payload);

      boolean published =
          mqttPublisher.publishLightCommand(deviceId, json);

      return new UpdateResult(
          LightSettingsResponseDTO.from(settings),
          published
      );

    } catch (JsonProcessingException exception) {
      throw new IllegalStateException(
          "Failed to serialize light command",
          exception
      );
    }
  }

  public record UpdateResult(
      LightSettingsResponseDTO settings,
      boolean mqttPublished
  ) {}
}
