package com.thusithakit.circleservice.controller;

import com.thusithakit.circleservice.dto.*;
import com.thusithakit.circleservice.service.CircleManagementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/circles")
public class CircleController {

  private final CircleManagementService circleService;

  public CircleController(
      CircleManagementService circleService
  ) {
    this.circleService = circleService;
  }

  @PostMapping
  public ResponseEntity<CircleResponseDTO> createCircle(
      @RequestHeader("X-User-Id") UUID userId,
      @Valid @RequestBody CreateCircleRequestDTO request
  ) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(circleService.createCircle(userId, request));
  }

  @GetMapping
  public ResponseEntity<List<CircleResponseDTO>> getMyCircles(
      @RequestHeader("X-User-Id") UUID userId
  ) {
    return ResponseEntity.ok(
        circleService.getMyCircles(userId)
    );
  }

  @GetMapping("/{circleId}/members")
  public ResponseEntity<List<CircleMemberResponseDTO>> getMembers(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID circleId
  ) {
    return ResponseEntity.ok(
        circleService.getMembers(userId, circleId)
    );
  }

  @PostMapping("/{circleId}/invitations")
  public ResponseEntity<InvitationResponseDTO> inviteMember(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID circleId,
      @Valid @RequestBody InviteMemberRequestDTO request
  ) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(circleService.inviteMember(
            userId,
            circleId,
            request
        ));
  }

  @DeleteMapping("/{circleId}/members/{memberUserId}")
  public ResponseEntity<Void> removeMember(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID circleId,
      @PathVariable UUID memberUserId
  ) {
    circleService.removeMember(
        userId,
        circleId,
        memberUserId
    );

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{circleId}/members/{memberUserId}/permissions")
  public ResponseEntity<Void> updateMemberPermission(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID circleId,
      @PathVariable UUID memberUserId,
      @Valid @RequestBody UpdateMemberPermissionRequest request
  ) {
    circleService.updateMemberPermission(
        userId,
        circleId,
        memberUserId,
        request
    );

    return ResponseEntity.noContent().build();
  }

  @PostMapping("/invitations/{invitationId}/accept")
  public ResponseEntity<Void> acceptInvitation(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID invitationId
  ) {
    circleService.acceptInvitation(
        userId,
        invitationId
    );

    return ResponseEntity.noContent().build();
  }
  @PostMapping("/{circleId}/devices")
  public ResponseEntity<CircleDeviceResponseDTO> addDevice(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID circleId,
      @Valid @RequestBody AddDeviceToCircleRequestDTO request
  ) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(circleService.addDeviceToCircle(
            userId,
            circleId,
            request
        ));
  }

  @GetMapping("/{circleId}/devices")
  public ResponseEntity<List<CircleDeviceResponseDTO>> getDevices(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID circleId
  ) {
    return ResponseEntity.ok(
        circleService.getCircleDevices(
            userId,
            circleId
        )
    );
  }

  @DeleteMapping("/{circleId}/devices/{deviceId}")
  public ResponseEntity<Void> removeDevice(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID circleId,
      @PathVariable UUID deviceId
  ) {
    circleService.removeDeviceFromCircle(
        userId,
        circleId,
        deviceId
    );

    return ResponseEntity.noContent().build();
  }
}
