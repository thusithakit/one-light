package com.thusithakit.circleservice.repository;

import com.thusithakit.circleservice.model.CircleMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CircleMemberRepository
        extends JpaRepository<CircleMember, UUID> {

    List<CircleMember> findAllByCircleId(UUID circleId);

    Optional<CircleMember> findByCircleIdAndUserId(
            UUID circleId,
            UUID userId
    );

    boolean existsByCircleIdAndUserId(
            UUID circleId,
            UUID userId
    );

    boolean existsByCircleIdAndUserIdAndCanSendMessagesTrue(
            UUID circleId,
            UUID userId
    );

    void deleteByCircleIdAndUserId(
            UUID circleId,
            UUID userId
    );
}
