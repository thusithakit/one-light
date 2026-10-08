package com.thusithakit.messageservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "messages",
    indexes = {
        @Index(
            name = "idx_messages_device_id",
            columnList = "device_id"
        ),
        @Index(
            name = "idx_messages_sender_user_id",
            columnList = "sender_user_id"
        ),
        @Index(
            name = "idx_messages_created_at",
            columnList = "created_at"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "device_id", nullable = false)
  private UUID deviceId;

  @Column(name = "sender_user_id", nullable = false)
  private UUID senderUserId;

  @Column(nullable = false, length = 500)
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private MessageStatus status;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "delivered_at")
  private Instant deliveredAt;

  @PrePersist
  protected void onCreate() {
    createdAt = Instant.now();

    if (status == null) {
      status = MessageStatus.PENDING;
    }
  }
}
