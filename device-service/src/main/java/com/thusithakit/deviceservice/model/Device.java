package com.thusithakit.deviceservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "devices",
        indexes = {
                @Index(
                        name = "idx_devices_owner_id",
                        columnList = "owner_id"
                ),
                @Index(
                        name = "idx_devices_identifier",
                        columnList = "device_identifier"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Null until the device is paired.
     */
    @Column(name = "owner_id")
    private UUID ownerId;

    /**
     * Permanent hardware identifier.
     */
    @Column(
            name = "device_identifier",
            nullable = false,
            unique = true,
            length = 100
    )
    private String deviceIdentifier;

    @Column(
            name = "name",
            nullable = false,
            length = 100
    )
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private DeviceStatus status;

    @Column(
            name = "firmware_version",
            length = 50
    )
    private String firmwareVersion;

    @Column(name = "last_seen_at")
    private Instant lastSeenAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {

        Instant now = Instant.now();

        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = DeviceStatus.OFFLINE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
