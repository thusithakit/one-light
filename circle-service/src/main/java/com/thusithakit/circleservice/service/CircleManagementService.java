package com.thusithakit.circleservice.service;

import com.thusithakit.circleservice.dto.*;
import com.thusithakit.circleservice.exception.CircleAccessDeniedException;
import com.thusithakit.circleservice.exception.CircleNotFoundException;
import com.thusithakit.circleservice.grpc.DeviceDetailsGrpcClient;
import com.thusithakit.circleservice.grpc.DeviceServiceGrpcClient;
import com.thusithakit.circleservice.mapper.CircleMapper;
import com.thusithakit.circleservice.mapper.InvitationMapper;
import com.thusithakit.circleservice.model.*;
import com.thusithakit.circleservice.repository.CircleDeviceRepository;
import com.thusithakit.circleservice.repository.CircleInvitationRepository;
import com.thusithakit.circleservice.repository.CircleMemberRepository;
import com.thusithakit.circleservice.repository.CircleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CircleManagementService {

  private final CircleRepository circleRepository;

  private final CircleMemberRepository circleMemberRepository;

  private final CircleInvitationRepository circleInvitationRepository;

  private final CircleDeviceRepository circleDeviceRepository;

  private final DeviceServiceGrpcClient deviceServiceGrpcClient;

  private final DeviceDetailsGrpcClient deviceDetailsGrpcClient;


  // =========================================================
  // CIRCLES
  // =========================================================

  @Transactional
  public CircleResponseDTO createCircle(
      UUID ownerId,
      CreateCircleRequestDTO request
  ) {

    Circle circle =
        Circle.builder()
            .ownerId(ownerId)
            .name(request.name().trim())
            .build();

    circle = circleRepository.save(circle);

    CircleMember owner =
        CircleMember.builder()
            .circleId(circle.getId())
            .userId(ownerId)
            .role(CircleMemberRole.OWNER)
            .canSendMessages(true)
            .build();

    circleMemberRepository.save(owner);

    return CircleMapper.toCircleResponse(circle);
  }


  /**
   * Returns every circle where the authenticated user
   * is a member.
   *
   * This includes circles owned by the user.
   */
  @Transactional(readOnly = true)
  public List<MyCircleResponseDTO> getMyCircles(
      UUID userId
  ) {

    return circleMemberRepository
        .findAllByUserId(userId)
        .stream()
        .map(member -> {

          Circle circle =
              circleRepository
                  .findById(member.getCircleId())
                  .orElseThrow(() ->
                      new CircleNotFoundException(
                          "Circle not found"
                      )
                  );

          return new MyCircleResponseDTO(
              circle.getId(),
              circle.getName(),
              circle.getOwnerId(),
              member.getRole(),
              member.isCanSendMessages(),
              member.getJoinedAt()
          );
        })
        .toList();
  }


  // =========================================================
  // INVITATIONS
  // =========================================================

  @Transactional
  public InvitationResponseDTO inviteMember(
      UUID ownerId,
      UUID circleId,
      InviteMemberRequestDTO request
  ) {

    Circle circle =
        getOwnedCircle(ownerId, circleId);

    UUID invitedUserId =
        request.userId();

    if (invitedUserId.equals(ownerId)) {
      throw new IllegalArgumentException(
          "You cannot invite yourself"
      );
    }

    if (circleMemberRepository
        .existsByCircleIdAndUserId(
            circleId,
            invitedUserId
        )) {

      throw new IllegalArgumentException(
          "User is already a member of this circle"
      );
    }

    if (circleInvitationRepository
        .existsByCircleIdAndInvitedUserIdAndStatus(
            circleId,
            invitedUserId,
            InvitationStatus.PENDING
        )) {

      throw new IllegalArgumentException(
          "A pending invitation already exists"
      );
    }

    CircleInvitation invitation =
        CircleInvitation.builder()
            .circleId(circleId)
            .invitedUserId(invitedUserId)
            .invitedBy(ownerId)
            .status(InvitationStatus.PENDING)
            .expiresAt(
                Instant.now()
                    .plus(7, ChronoUnit.DAYS)
            )
            .build();

    invitation =
        circleInvitationRepository.save(invitation);

    return InvitationMapper.toInvitationResponse(
        invitation,
        circle.getName()
    );
  }


  /**
   * Returns pending invitations for the authenticated user.
   */
  @Transactional(readOnly = true)
  public List<InvitationResponseDTO>
  getMyPendingInvitations(UUID userId) {

    Instant now = Instant.now();

    return circleInvitationRepository
        .findAllByInvitedUserIdAndStatusOrderByCreatedAtDesc(
            userId,
            InvitationStatus.PENDING
        )
        .stream()
        .map(invitation -> {

          Circle circle =
              circleRepository
                  .findById(
                      invitation.getCircleId()
                  )
                  .orElse(null);

          if (circle == null) {
            return null;
          }

          /*
           * We don't modify expired invitations inside
           * this read operation.
           *
           * They will be marked EXPIRED when the user
           * attempts to accept them.
           */
          return InvitationMapper.toInvitationResponse(
              invitation,
              circle.getName()
          );
        })
        .filter(response -> response != null)
        .toList();
  }


  @Transactional
  public InvitationResponseDTO acceptInvitation(
      UUID userId,
      UUID invitationId
  ) {

    CircleInvitation invitation =
        circleInvitationRepository
            .findByIdAndInvitedUserId(
                invitationId,
                userId
            )
            .orElseThrow(() ->
                new CircleAccessDeniedException(
                    "Invitation not found"
                )
            );

    if (invitation.getStatus()
        != InvitationStatus.PENDING) {

      throw new IllegalArgumentException(
          "Invitation is no longer pending"
      );
    }

    if (invitation.getExpiresAt()
        .isBefore(Instant.now())) {

      invitation.setStatus(
          InvitationStatus.EXPIRED
      );

      circleInvitationRepository.save(
          invitation
      );

      throw new IllegalArgumentException(
          "Invitation has expired"
      );
    }

    if (circleMemberRepository
        .existsByCircleIdAndUserId(
            invitation.getCircleId(),
            userId
        )) {

      invitation.setStatus(
          InvitationStatus.ACCEPTED
      );

      circleInvitationRepository.save(
          invitation
      );

      throw new IllegalArgumentException(
          "You are already a member of this circle"
      );
    }

    CircleMember member =
        CircleMember.builder()
            .circleId(invitation.getCircleId())
            .userId(userId)
            .role(CircleMemberRole.MEMBER)
            .canSendMessages(true)
            .build();

    circleMemberRepository.save(member);

    invitation.setStatus(
        InvitationStatus.ACCEPTED
    );

    circleInvitationRepository.save(
        invitation
    );

    Circle circle =
        circleRepository
            .findById(invitation.getCircleId())
            .orElseThrow(() ->
                new CircleNotFoundException(
                    "Circle not found"
                )
            );

    return InvitationMapper.toInvitationResponse(
        invitation,
        circle.getName()
    );
  }


  @Transactional
  public InvitationResponseDTO declineInvitation(
      UUID userId,
      UUID invitationId
  ) {

    CircleInvitation invitation =
        circleInvitationRepository
            .findByIdAndInvitedUserId(
                invitationId,
                userId
            )
            .orElseThrow(() ->
                new CircleAccessDeniedException(
                    "Invitation not found"
                )
            );

    if (invitation.getStatus()
        != InvitationStatus.PENDING) {

      throw new IllegalArgumentException(
          "Invitation is no longer pending"
      );
    }

    invitation.setStatus(
        InvitationStatus.DECLINED
    );

    circleInvitationRepository.save(
        invitation
    );

    Circle circle =
        circleRepository
            .findById(invitation.getCircleId())
            .orElseThrow(() ->
                new CircleNotFoundException(
                    "Circle not found"
                )
            );

    return InvitationMapper.toInvitationResponse(
        invitation,
        circle.getName()
    );
  }


  // =========================================================
  // MEMBERS
  // =========================================================

  @Transactional(readOnly = true)
  public List<CircleMemberResponseDTO> getMembers(
      UUID ownerId,
      UUID circleId
  ) {

    getOwnedCircle(ownerId, circleId);

    return circleMemberRepository
        .findAllByCircleId(circleId)
        .stream()
        .map(member ->
            new CircleMemberResponseDTO(
                member.getUserId(),
                member.getRole(),
                member.isCanSendMessages(),
                member.getJoinedAt()
            )
        )
        .toList();
  }


  @Transactional
  public void removeMember(
      UUID ownerId,
      UUID circleId,
      UUID userId
  ) {

    getOwnedCircle(ownerId, circleId);

    if (ownerId.equals(userId)) {
      throw new IllegalArgumentException(
          "The circle owner cannot be removed"
      );
    }

    if (!circleMemberRepository
        .existsByCircleIdAndUserId(
            circleId,
            userId
        )) {

      throw new IllegalArgumentException(
          "User is not a member of this circle"
      );
    }

    circleMemberRepository
        .deleteByCircleIdAndUserId(
            circleId,
            userId
        );
  }


  @Transactional
  public CircleMemberResponseDTO updateMemberPermission(
      UUID ownerId,
      UUID circleId,
      UUID userId,
      UpdateMemberPermissionRequest request
  ) {

    getOwnedCircle(ownerId, circleId);

    if (ownerId.equals(userId)) {
      throw new IllegalArgumentException(
          "Owner permissions cannot be modified"
      );
    }

    CircleMember member =
        circleMemberRepository
            .findByCircleIdAndUserId(
                circleId,
                userId
            )
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "User is not a member"
                )
            );

    member.setCanSendMessages(
        request.canSendMessages()
    );

    member =
        circleMemberRepository.save(member);

    return new CircleMemberResponseDTO(
        member.getUserId(),
        member.getRole(),
        member.isCanSendMessages(),
        member.getJoinedAt()
    );
  }


  // =========================================================
  // DEVICES
  // =========================================================

  @Transactional
  public CircleDeviceResponseDTO addDeviceToCircle(
      UUID ownerId,
      UUID circleId,
      AddDeviceToCircleRequestDTO request
  ) {

    getOwnedCircle(ownerId, circleId);

    boolean ownsDevice =
        deviceServiceGrpcClient
            .verifyDeviceOwnership(
                ownerId,
                request.deviceId()
            );

    if (!ownsDevice) {
      throw new CircleAccessDeniedException(
          "You do not own this device"
      );
    }

    if (circleDeviceRepository
        .existsByCircleIdAndDeviceId(
            circleId,
            request.deviceId()
        )) {

      throw new IllegalArgumentException(
          "Device is already associated with this circle"
      );
    }

    CircleDevice circleDevice =
        CircleDevice.builder()
            .circleId(circleId)
            .deviceId(request.deviceId())
            .build();

    circleDevice =
        circleDeviceRepository.save(
            circleDevice
        );

    return new CircleDeviceResponseDTO(
        circleDevice.getDeviceId(),
        circleDevice.getAddedAt()
    );
  }


  @Transactional(readOnly = true)
  public List<CircleDeviceResponseDTO> getCircleDevices(
      UUID ownerId,
      UUID circleId
  ) {

    getOwnedCircle(ownerId, circleId);

    return circleDeviceRepository
        .findAllByCircleId(circleId)
        .stream()
        .map(device ->
            new CircleDeviceResponseDTO(
                device.getDeviceId(),
                device.getAddedAt()
            )
        )
        .toList();
  }


  /**
   * Returns all devices accessible to the authenticated user.
   *
   * This is the endpoint the PWA should use when displaying
   * OneLights available to a member.
   */
  @Transactional(readOnly = true)
  public List<MyDeviceResponseDTO> getMyDevices(UUID userId) {

    List<CircleMember> memberships =
        circleMemberRepository.findAllByUserId(userId);

    Map<UUID, MyDeviceResponseDTO> devices = new LinkedHashMap<>();

    for (CircleMember membership : memberships) {

      Circle circle = circleRepository.findById(
              membership.getCircleId()
          )
          .orElse(null);

      if (circle == null) {
        continue;
      }

      List<CircleDevice> circleDevices =
          circleDeviceRepository.findAllByCircleId(
              membership.getCircleId()
          );

      for (CircleDevice circleDevice : circleDevices) {

        UUID deviceId = circleDevice.getDeviceId();

        DeviceDetailsGrpcClient.DeviceDetails device;

        try {
          device = deviceDetailsGrpcClient.getDeviceDetails(deviceId);
        } catch (RuntimeException e) {
          continue;
        }

        MyDeviceResponseDTO existing = devices.get(deviceId);

        boolean canSendMessages =
            membership.isCanSendMessages();

        if (existing == null) {

          devices.put(
              deviceId,
              new MyDeviceResponseDTO(
                  device.deviceId(),
                  device.name(),
                  device.status(),
                  device.firmwareVersion(),
                  device.lastSeenAt(),
                  circle.getId(),
                  circle.getName(),
                  canSendMessages
              )
          );

        } else if (!existing.canSendMessages() && canSendMessages) {

          /*
           * User has access through multiple circles.
           * If at least one circle grants send permission,
           * they can send messages to this device.
           */
          devices.put(
              deviceId,
              new MyDeviceResponseDTO(
                  existing.deviceId(),
                  existing.name(),
                  existing.status(),
                  existing.firmwareVersion(),
                  existing.lastSeenAt(),
                  existing.circleId(),
                  existing.circleName(),
                  true
              )
          );
        }
      }
    }

    return new ArrayList<>(devices.values());
  }


  @Transactional
  public void removeDeviceFromCircle(
      UUID ownerId,
      UUID circleId,
      UUID deviceId
  ) {

    getOwnedCircle(ownerId, circleId);

    if (!circleDeviceRepository
        .existsByCircleIdAndDeviceId(
            circleId,
            deviceId
        )) {

      throw new IllegalArgumentException(
          "Device is not associated with this circle"
      );
    }

    circleDeviceRepository
        .deleteByCircleIdAndDeviceId(
            circleId,
            deviceId
        );
  }


  // =========================================================
  // MESSAGE AUTHORIZATION
  // =========================================================

  @Transactional(readOnly = true)
  public boolean canUserSendMessage(
      UUID userId,
      UUID deviceId
  ) {

    return circleDeviceRepository
        .findAllByDeviceId(deviceId)
        .stream()
        .anyMatch(circleDevice ->
            circleMemberRepository
                .existsByCircleIdAndUserIdAndCanSendMessagesTrue(
                    circleDevice.getCircleId(),
                    userId
                )
        );
  }


  // =========================================================
  // HELPERS
  // =========================================================

  private Circle getOwnedCircle(
      UUID ownerId,
      UUID circleId
  ) {

    return circleRepository
        .findById(circleId)
        .filter(circle ->
            circle.getOwnerId()
                .equals(ownerId)
        )
        .orElseThrow(() ->
            new CircleAccessDeniedException(
                "You do not own this circle"
            )
        );
  }

}
