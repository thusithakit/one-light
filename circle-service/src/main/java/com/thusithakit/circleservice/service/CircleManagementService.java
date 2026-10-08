package com.thusithakit.circleservice.service;

import com.thusithakit.circleservice.dto.*;
import com.thusithakit.circleservice.exception.CircleAccessDeniedException;
import com.thusithakit.circleservice.exception.CircleNotFoundException;
import com.thusithakit.circleservice.grpc.DeviceServiceGrpcClient;
import com.thusithakit.circleservice.mapper.CircleMapper;
import com.thusithakit.circleservice.mapper.InvitationMapper;
import com.thusithakit.circleservice.model.*;
import com.thusithakit.circleservice.repository.CircleDeviceRepository;
import com.thusithakit.circleservice.repository.CircleInvitationRepository;
import com.thusithakit.circleservice.repository.CircleMemberRepository;
import com.thusithakit.circleservice.repository.CircleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CircleManagementService {

  private final CircleRepository circleRepository;
  private final CircleMemberRepository memberRepository;
  private final CircleInvitationRepository invitationRepository;
  private final CircleDeviceRepository circleDeviceRepository;
  private final DeviceServiceGrpcClient deviceServiceGrpcClient;

  public CircleManagementService(
      CircleRepository circleRepository,
      CircleMemberRepository memberRepository,
      CircleInvitationRepository invitationRepository,
      CircleDeviceRepository circleDeviceRepository,
      DeviceServiceGrpcClient deviceServiceGrpcClient
  ) {
    this.circleRepository = circleRepository;
    this.memberRepository = memberRepository;
    this.invitationRepository = invitationRepository;
    this.circleDeviceRepository = circleDeviceRepository;
    this.deviceServiceGrpcClient = deviceServiceGrpcClient;
  }

  @Transactional
  public CircleResponseDTO createCircle(
      UUID ownerId,
      CreateCircleRequestDTO request
  ) {
    Circle circle = Circle.builder()
        .ownerId(ownerId)
        .name(request.name())
        .build();

    circle = circleRepository.save(circle);

    CircleMember owner = CircleMember.builder()
        .circleId(circle.getId())
        .userId(ownerId)
        .role(CircleMemberRole.OWNER)
        .canSendMessages(true)
        .build();

    memberRepository.save(owner);

    return CircleMapper.toResponse(circle);
  }

  @Transactional(readOnly = true)
  public List<CircleResponseDTO> getMyCircles(UUID ownerId) {
    return circleRepository.findAllByOwnerId(ownerId)
        .stream()
        .map(CircleMapper::toResponse)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<CircleMemberResponseDTO> getMembers(
      UUID ownerId,
      UUID circleId
  ) {
    getOwnedCircle(ownerId, circleId);

    return memberRepository.findAllByCircleId(circleId)
        .stream()
        .map(member -> new CircleMemberResponseDTO(
            member.getUserId(),
            member.getRole(),
            member.isCanSendMessages(),
            member.getJoinedAt()
        ))
        .toList();
  }

  @Transactional
  public InvitationResponseDTO inviteMember(
      UUID ownerId,
      UUID circleId,
      InviteMemberRequestDTO request
  ) {
    getOwnedCircle(ownerId, circleId);

    if (memberRepository.existsByCircleIdAndUserId(
        circleId,
        request.userId()
    )) {
      throw new IllegalArgumentException(
          "User is already a member of this circle"
      );
    }

    CircleInvitation invitation = CircleInvitation.builder()
        .circleId(circleId)
        .invitedUserId(request.userId())
        .invitedBy(ownerId)
        .status(InvitationStatus.PENDING)
        .expiresAt(Instant.now().plus(Duration.ofDays(7)))
        .build();

    invitation = invitationRepository.save(invitation);

    return InvitationMapper.toInvitationResponse(invitation);
  }

  @Transactional
  public void removeMember(
      UUID ownerId,
      UUID circleId,
      UUID userId
  ) {
    getOwnedCircle(ownerId, circleId);

    CircleMember member = memberRepository
        .findByCircleIdAndUserId(circleId, userId)
        .orElseThrow(() ->
            new IllegalArgumentException(
                "User is not a member of this circle"
            ));

    if (member.getRole() == CircleMemberRole.OWNER) {
      throw new IllegalArgumentException(
          "The owner cannot be removed from the circle"
      );
    }

    memberRepository.delete(member);
  }

  @Transactional
  public void updateMemberPermission(
      UUID ownerId,
      UUID circleId,
      UUID userId,
      UpdateMemberPermissionRequest request
  ) {
    getOwnedCircle(ownerId, circleId);

    CircleMember member = memberRepository
        .findByCircleIdAndUserId(circleId, userId)
        .orElseThrow(() ->
            new IllegalArgumentException(
                "User is not a member of this circle"
            ));

    if (member.getRole() == CircleMemberRole.OWNER) {
      throw new IllegalArgumentException(
          "Owner permissions cannot be changed"
      );
    }

    member.setCanSendMessages(request.canSendMessages());

    memberRepository.save(member);
  }

  @Transactional
  public void acceptInvitation(
      UUID userId,
      UUID invitationId
  ) {
    CircleInvitation invitation =
        invitationRepository
            .findByIdAndInvitedUserId(
                invitationId,
                userId
            )
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Invitation not found"
                ));

    if (invitation.getStatus() != InvitationStatus.PENDING) {
      throw new IllegalArgumentException(
          "Invitation is no longer pending"
      );
    }

    if (invitation.getExpiresAt().isBefore(Instant.now())) {
      invitation.setStatus(InvitationStatus.EXPIRED);
      invitationRepository.save(invitation);

      throw new IllegalArgumentException(
          "Invitation has expired"
      );
    }

    if (memberRepository.existsByCircleIdAndUserId(
        invitation.getCircleId(),
        userId
    )) {
      throw new IllegalArgumentException(
          "User is already a member"
      );
    }

    CircleMember member = CircleMember.builder()
        .circleId(invitation.getCircleId())
        .userId(userId)
        .role(CircleMemberRole.MEMBER)
        .canSendMessages(true)
        .build();

    memberRepository.save(member);

    invitation.setStatus(InvitationStatus.ACCEPTED);
    invitationRepository.save(invitation);
  }

  @Transactional(readOnly = true)
  public boolean canUserSendMessages(
      UUID userId,
      UUID circleId
  ) {
    return memberRepository
        .existsByCircleIdAndUserIdAndCanSendMessagesTrue(
            circleId,
            userId
        );
  }

  private Circle getOwnedCircle(
      UUID ownerId,
      UUID circleId
  ) {
    Circle circle = circleRepository
        .findById(circleId)
        .orElseThrow(() ->
            new CircleNotFoundException(
                "Circle not found"
            ));

    if (!circle.getOwnerId().equals(ownerId)) {
      throw new CircleAccessDeniedException(
          "You do not own this circle"
      );
    }

    return circle;
  }

  @Transactional
  public CircleDeviceResponseDTO addDeviceToCircle(
      UUID ownerId,
      UUID circleId,
      AddDeviceToCircleRequestDTO request
  ) {

    // 1. Verify that the user owns the circle
    getOwnedCircle(ownerId, circleId);

    // 2. Verify that the user owns the device
    boolean ownsDevice =
        deviceServiceGrpcClient.verifyDeviceOwnership(
            ownerId,
            request.deviceId()
        );

    if (!ownsDevice) {
      throw new CircleAccessDeniedException(
          "You do not own this device"
      );
    }

    // 3. Prevent duplicate device association
    if (circleDeviceRepository.existsByCircleIdAndDeviceId(
        circleId,
        request.deviceId()
    )) {
      throw new IllegalArgumentException(
          "Device is already associated with this circle"
      );
    }

    // 4. Associate device with circle
    CircleDevice circleDevice =
        CircleDevice.builder()
            .circleId(circleId)
            .deviceId(request.deviceId())
            .build();

    circleDevice =
        circleDeviceRepository.save(circleDevice);

    return new CircleDeviceResponseDTO(
        circleDevice.getDeviceId(),
        circleDevice.getAddedAt()
    );
  }

  @Transactional
  public void removeDeviceFromCircle(
      UUID ownerId,
      UUID circleId,
      UUID deviceId
  ) {
    getOwnedCircle(ownerId, circleId);

    if (!circleDeviceRepository.existsByCircleIdAndDeviceId(
        circleId,
        deviceId
    )) {
      throw new IllegalArgumentException(
          "Device is not associated with this circle"
      );
    }

    circleDeviceRepository.deleteByCircleIdAndDeviceId(
        circleId,
        deviceId
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
        .map(device -> new CircleDeviceResponseDTO(
            device.getDeviceId(),
            device.getAddedAt()
        ))
        .toList();
  }

  @Transactional(readOnly = true)
  public boolean canUserSendMessage(
      UUID userId,
      UUID deviceId
  ) {
    List<CircleDevice> deviceAccess =
        circleDeviceRepository.findAllByDeviceId(deviceId);

    for (CircleDevice access : deviceAccess) {

      boolean canSend =
          memberRepository
              .existsByCircleIdAndUserIdAndCanSendMessagesTrue(
                  access.getCircleId(),
                  userId
              );

      if (canSend) {
        return true;
      }
    }

    return false;
  }
}
