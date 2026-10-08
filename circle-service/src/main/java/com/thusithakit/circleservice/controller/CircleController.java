package com.thusithakit.circleservice.controller;

import com.thusithakit.circleservice.dto.*;
import com.thusithakit.circleservice.service.CircleManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/circles")
@RequiredArgsConstructor
public class CircleController {

  private final CircleManagementService circleManagementService;


  // =========================================================
  // CIRCLES
  // =========================================================

  @PostMapping
  public CircleResponseDTO createCircle(
      @RequestHeader("X-User-Id") UUID userId,
      @Valid @RequestBody CreateCircleRequestDTO request
  ) {

    return circleManagementService.createCircle(
        userId,
        request
    );
  }


  /**
   * Returns circles where the authenticated user is a member.
   */
  @GetMapping
  public List<MyCircleResponseDTO> getMyCircles(
      @RequestHeader("X-User-Id") UUID userId
  ) {

    return circleManagementService.getMyCircles(
        userId
    );
  }


  /**
   * Returns devices accessible to the authenticated user
   * through their circles.
   */
  @GetMapping("/my-devices")
  public List<MyDeviceResponseDTO> getMyDevices(
      @RequestHeader("X-User-Id") UUID userId
  ) {

    return circleManagementService.getMyDevices(
        userId
    );
  }


  // =========================================================
  // INVITATIONS
  // =========================================================

  /**
   * Get pending invitations for the authenticated user.
   */
  @GetMapping("/invitations")
  public List<InvitationResponseDTO> getMyPendingInvitations(
      @RequestHeader("X-User-Id") UUID userId
  ) {

    return circleManagementService
        .getMyPendingInvitations(userId);
  }


  /**
   * Accept invitation.
   */
  @PostMapping("/invitations/{invitationId}/accept")
  public InvitationResponseDTO acceptInvitation(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID invitationId
  ) {

    return circleManagementService
        .acceptInvitation(
            userId,
            invitationId
        );
  }


  /**
   * Decline invitation.
   */
  @PostMapping("/invitations/{invitationId}/decline")
  public InvitationResponseDTO declineInvitation(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID invitationId
  ) {

    return circleManagementService
        .declineInvitation(
            userId,
            invitationId
        );
  }


  // =========================================================
  // MEMBERS
  // =========================================================

  @GetMapping("/{circleId}/members")
  public List<CircleMemberResponseDTO> getMembers(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID circleId
  ) {

    return circleManagementService.getMembers(
        userId,
        circleId
    );
  }


  @PostMapping("/{circleId}/invitations")
  public InvitationResponseDTO inviteMember(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID circleId,
      @Valid @RequestBody InviteMemberRequestDTO request
  ) {

    return circleManagementService.inviteMember(
        userId,
        circleId,
        request
    );
  }


  @DeleteMapping(
      "/{circleId}/members/{memberUserId}"
  )
  public void removeMember(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID circleId,
      @PathVariable UUID memberUserId
  ) {

    circleManagementService.removeMember(
        userId,
        circleId,
        memberUserId
    );
  }


  @PatchMapping(
      "/{circleId}/members/{memberUserId}/permissions"
  )
  public CircleMemberResponseDTO updateMemberPermission(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID circleId,
      @PathVariable UUID memberUserId,
      @Valid @RequestBody
      UpdateMemberPermissionRequest request
  ) {

    return circleManagementService
        .updateMemberPermission(
            userId,
            circleId,
            memberUserId,
            request
        );
  }


  // =========================================================
  // DEVICES
  // =========================================================

  @GetMapping("/{circleId}/devices")
  public List<CircleDeviceResponseDTO> getCircleDevices(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID circleId
  ) {

    return circleManagementService
        .getCircleDevices(
            userId,
            circleId
        );
  }


  @PostMapping("/{circleId}/devices")
  public CircleDeviceResponseDTO addDeviceToCircle(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID circleId,
      @Valid @RequestBody
      AddDeviceToCircleRequestDTO request
  ) {

    return circleManagementService
        .addDeviceToCircle(
            userId,
            circleId,
            request
        );
  }


  @DeleteMapping(
      "/{circleId}/devices/{deviceId}"
  )
  public void removeDeviceFromCircle(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID circleId,
      @PathVariable UUID deviceId
  ) {

    circleManagementService
        .removeDeviceFromCircle(
            userId,
            circleId,
            deviceId
        );
  }
}
