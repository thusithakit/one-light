package com.thusithakit.deliveryservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "device_deliveries",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_device_deliveries_message_id",
            columnNames = "message_id"
        )
    },
    indexes = {
        @Index(name = "idx_device_deliveries_device_id", columnList = "device_id"),
        @Index(name = "idx_device_deliveries_status", columnList = "status")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceDelivery {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "message_id", nullable = false, unique = true, length = 100)
  private String messageId;

  @Column(name = "device_id", nullable = false, length = 100)
  private String deviceId;

  @Column(name = "status", nullable = false, length = 20)
  private String status;

  @Column(name = "failure_reason", length = 1000)
  private String failureReason;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "published_at")
  private Instant publishedAt;
}
