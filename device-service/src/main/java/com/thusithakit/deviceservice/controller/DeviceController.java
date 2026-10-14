package com.thusithakit.deviceservice.controller;

import com.thusithakit.deviceservice.dto.*;
import com.thusithakit.deviceservice.repository.DeviceRepository;
import com.thusithakit.deviceservice.service.DeviceService;
import com.thusithakit.deviceservice.service.LightSettingsService;
import com.thusithakit.deviceservice.service.PairingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/devices")
public class DeviceController {
    private final DeviceService deviceService;
    private final PairingService pairingService;
    private final LightSettingsService lightSettingsService;

    public DeviceController(DeviceService deviceService, PairingService pairingService, LightSettingsService lightSettingsService) {
        this.deviceService = deviceService;
        this.pairingService = pairingService;
        this.lightSettingsService = lightSettingsService;
    }

    @GetMapping
    public ResponseEntity<List<DeviceResponseDTO>> getDevices(
            @RequestHeader("X-User-Id") UUID ownerId
    ) {
        List<DeviceResponseDTO> response = deviceService.getAllDevices(ownerId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceResponseDTO> getDevice(
            @RequestHeader("X-User-Id") UUID ownerId,
            @PathVariable UUID deviceId
    ){
        DeviceResponseDTO response = deviceService.getDevice(ownerId, deviceId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{deviceId}")
    public ResponseEntity<DeviceResponseDTO> updateDevice(
            @RequestHeader("X-User-Id") UUID ownerId,
            @PathVariable UUID deviceId,
            @Valid @RequestBody UpdateDeviceRequestDTO request
    ){
        DeviceResponseDTO response = deviceService.updateDevice(ownerId, deviceId, request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> deleteDevice(
            @RequestHeader("X-User-Id") UUID ownerId,
            @PathVariable UUID deviceId
    ){
        deviceService.deleteDevice(ownerId, deviceId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/provision")
    public ResponseEntity<CreatePairingSessionResponseDTO> provisionDevice(
            @Valid @RequestBody CreatePairingSessionRequestDTO request
    ) {

        CreatePairingSessionResponseDTO response =
                pairingService.createPairingSession(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/pair")
    public ResponseEntity<PairDeviceResponseDTO> pairDevice(
            @RequestHeader("X-User-Id") UUID ownerId,
            @Valid @RequestBody PairDeviceRequestDTO request
    ) {

        PairDeviceResponseDTO response =
                pairingService.pairDevice(
                        ownerId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{deviceId}/light")
    public LightSettingsResponseDTO get(
        @PathVariable UUID deviceId
    ) {
        return lightSettingsService.get(deviceId);
    }

    @PatchMapping("/{deviceId}/light")
    public ResponseEntity<?> update(
        @PathVariable UUID deviceId,
        @Valid @RequestBody LightSettingsRequestDTO request
    ) {
        LightSettingsService.UpdateResult result =
            lightSettingsService.update(deviceId, request);

        HttpStatus status = result.mqttPublished()
            ? HttpStatus.OK
            : HttpStatus.ACCEPTED;

        return ResponseEntity.status(status).body(Map.of(
            "settings", result.settings(),
            "mqttPublished", result.mqttPublished(),
            "message", result.mqttPublished()
                ? "Settings saved and MQTT command published"
                : "Settings saved, but MQTT publication failed"
        ));
    }

}
