package com.thusithakit.deviceservice.repository;

import com.thusithakit.deviceservice.model.PairingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface PairingSessionRepository extends JpaRepository<PairingSession, UUID> {

    Optional<PairingSession> findByCode(String code);

    Optional<PairingSession> findByDeviceIdAndUsedFalse(
            UUID deviceId
    );
    Optional<PairingSession> findFirstByDeviceIdAndUsedFalseAndExpiresAtAfter(
        UUID deviceId,
        Instant now
    );
}
