package com.thusithakit.circleservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "circle_devices",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_circle_device",
            columnNames = {"circle_id", "device_id"}
        )
    },
    indexes = {
        @Index(
            name = "idx_circle_devices_circle_id",
            columnList = "circle_id"
        ),
        @Index(
            name = "idx_circle_devices_device_id",
            columnList = "device_id"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CircleDevice {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "circle_id", nullable = false)
  private UUID circleId;

  @Column(name = "device_id", nullable = false)
  private UUID deviceId;

  @Column(name = "added_at", nullable = false)
  private Instant addedAt;

  @PrePersist
  protected void onCreate() {
    addedAt = Instant.now();
  }
}
