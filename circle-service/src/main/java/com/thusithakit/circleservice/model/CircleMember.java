package com.thusithakit.circleservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "circle_members",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_circle_member",
                        columnNames = {"circle_id", "user_id"}
                )
        },
        indexes = {
                @Index(name = "idx_circle_members_circle_id", columnList = "circle_id"),
                @Index(name = "idx_circle_members_user_id", columnList = "user_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CircleMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "circle_id", nullable = false)
    private UUID circleId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CircleMemberRole role;

    @Column(name = "can_send_messages", nullable = false)
    private boolean canSendMessages;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    @PrePersist
    protected void onCreate() {
        joinedAt = Instant.now();
    }
}
