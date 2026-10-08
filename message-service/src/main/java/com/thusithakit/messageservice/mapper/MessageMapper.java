package com.thusithakit.messageservice.mapper;

import com.thusithakit.messageservice.dto.MessageResponseDTO;
import com.thusithakit.messageservice.model.Message;

public class MessageMapper {
  public static MessageResponseDTO toResponse(
      Message message
  ) {

    return new MessageResponseDTO(
        message.getId(),
        message.getDeviceId(),
        message.getSenderUserId(),
        message.getContent(),
        message.getStatus(),
        message.getCreatedAt(),
        message.getDeliveredAt()
    );
  }
}
