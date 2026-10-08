package com.thusithakit.circleservice.mapper;

import com.thusithakit.circleservice.dto.InvitationResponseDTO;
import com.thusithakit.circleservice.model.CircleInvitation;

public class InvitationMapper {
  public static InvitationResponseDTO toInvitationResponse(
      CircleInvitation invitation
  ) {
    return new InvitationResponseDTO(
        invitation.getId(),
        invitation.getCircleId(),
        invitation.getInvitedUserId(),
        invitation.getInvitedBy(),
        invitation.getStatus(),
        invitation.getExpiresAt()
    );
  }
}
