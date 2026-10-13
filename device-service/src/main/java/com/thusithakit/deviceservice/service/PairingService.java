package com.thusithakit.deviceservice.service;

import com.thusithakit.deviceservice.dto.CreatePairingSessionRequestDTO;
import com.thusithakit.deviceservice.dto.CreatePairingSessionResponseDTO;
import com.thusithakit.deviceservice.dto.PairDeviceRequestDTO;
import com.thusithakit.deviceservice.dto.PairDeviceResponseDTO;
import com.thusithakit.deviceservice.model.Device;
import com.thusithakit.deviceservice.model.DeviceStatus;
import com.thusithakit.deviceservice.model.PairingSession;
import com.thusithakit.deviceservice.repository.DeviceRepository;
import com.thusithakit.deviceservice.repository.PairingSessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PairingService {

    private final DeviceRepository deviceRepository;
    private final PairingSessionRepository pairingSessionRepository;

    public PairingService(
            DeviceRepository deviceRepository,
            PairingSessionRepository pairingSessionRepository
    ) {
        this.deviceRepository = deviceRepository;
        this.pairingSessionRepository = pairingSessionRepository;
    }

    /**
     * Called by the physical device after it has Internet access.
     */
    @Transactional
    public CreatePairingSessionResponseDTO createPairingSession(
        CreatePairingSessionRequestDTO request
    ) {
        Device device = deviceRepository
            .findByDeviceIdentifier(request.deviceIdentifier())
            .orElseGet(() -> {
                Device newDevice = Device.builder()
                    .deviceIdentifier(request.deviceIdentifier())
                    .name("OneLight")
                    .status(DeviceStatus.UNPAIRED)
                    .firmwareVersion(request.firmwareVersion())
                    .build();

                return deviceRepository.save(newDevice);
            });

        device.setFirmwareVersion(request.firmwareVersion());
        deviceRepository.save(device);

        // Already paired: return the existing device ID.
        if (device.getOwnerId() != null) {
            return new CreatePairingSessionResponseDTO(
                device.getId(),
                null,
                null,
                true
            );
        }

        // Reuse an active pairing session if one exists.
        Instant now = Instant.now();

        PairingSession session = pairingSessionRepository
            .findFirstByDeviceIdAndUsedFalseAndExpiresAtAfter(
                device.getId(),
                now
            )
            .orElseGet(() -> {
                PairingSession newSession = PairingSession.builder()
                    .deviceId(device.getId())
                    .code(generateCode())
                    .expiresAt(now.plus(Duration.ofMinutes(10)))
                    .used(false)
                    .build();

                return pairingSessionRepository.save(newSession);
            });

        return new CreatePairingSessionResponseDTO(
            device.getId(),
            session.getCode(),
            session.getExpiresAt(),
            false
        );
    }

    /**
     * Called by authenticated PWA user.
     */
    @Transactional
    public PairDeviceResponseDTO pairDevice(
            UUID ownerId,
            PairDeviceRequestDTO request
    ) {

        PairingSession session =
                pairingSessionRepository
                        .findByCode(request.pairingCode())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid pairing code"
                                )
                        );

        if (session.isUsed()) {
            throw new IllegalArgumentException(
                    "Pairing code has already been used"
            );
        }

        if (session.getExpiresAt()
                .isBefore(Instant.now())) {

            throw new IllegalArgumentException(
                    "Pairing code has expired"
            );
        }

        Device device =
                deviceRepository
                        .findById(session.getDeviceId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Device not found"
                                )
                        );

        if (device.getOwnerId() != null) {
            throw new IllegalArgumentException(
                    "Device is already paired"
            );
        }

        device.setOwnerId(ownerId);
        device.setStatus(DeviceStatus.OFFLINE);

        deviceRepository.save(device);

        session.setUsed(true);

        pairingSessionRepository.save(session);

        return new PairDeviceResponseDTO(
                device.getId(),
                device.getDeviceIdentifier(),
                device.getName(),
                device.getStatus()
        );
    }

    private String generateCode() {

        int number =
                ThreadLocalRandom.current()
                        .nextInt(100000, 1000000);

        return String.valueOf(number);
    }
}
