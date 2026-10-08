package com.thusithakit.circleservice.repository;

import com.thusithakit.circleservice.model.CircleInvitation;
import com.thusithakit.circleservice.model.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CircleInvitationRepository
    extends JpaRepository<CircleInvitation, UUID> {

    List<CircleInvitation> findAllByInvitedUserIdAndStatusOrderByCreatedAtDesc(
        UUID invitedUserId,
        InvitationStatus status
    );

    Optional<CircleInvitation> findByIdAndInvitedUserId(
        UUID id,
        UUID invitedUserId
    );

    boolean existsByCircleIdAndInvitedUserIdAndStatus(
        UUID circleId,
        UUID invitedUserId,
        InvitationStatus status
    );
}
