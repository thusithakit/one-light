package com.thusithakit.deviceservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "device_light_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LightSettings {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "device_id", nullable = false, unique = true)
  private Device device;

  @Column(name = "is_on", nullable = false)
  private boolean on = true;

  @Column(nullable = false)
  private int brightness = 80;

  @Column(nullable = false, length = 7)
  private String color = "#FFB464";

  @Column(nullable = false)
  private Instant updatedAt = Instant.now();

  @PrePersist
  @PreUpdate
  void updateTimestamp() {
    updatedAt = Instant.now();
  }

  public LightSettings(Device device) {
    this.device = device;
  }
}
