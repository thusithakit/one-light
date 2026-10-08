package com.thusithakit.messageservice.service;

import com.thusithakit.messageservice.dto.MessageCreatedEventDTO;
import com.thusithakit.messageservice.dto.MessageResponseDTO;
import com.thusithakit.messageservice.dto.SendMessageRequestDTO;
import com.thusithakit.messageservice.exception.MessageAccessDeniedException;
import com.thusithakit.messageservice.grpc.CircleServiceGrpcClient;
import com.thusithakit.messageservice.kafka.MessageEventPublisher;
import com.thusithakit.messageservice.mapper.MessageMapper;
import com.thusithakit.messageservice.model.Message;
import com.thusithakit.messageservice.model.MessageStatus;
import com.thusithakit.messageservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;

  private final CircleServiceGrpcClient
      circleServiceGrpcClient;

  private final MessageEventPublisher
      messageEventPublisher;

  @Transactional
  public MessageResponseDTO sendMessage(
      UUID senderUserId,
      SendMessageRequestDTO request
  ) {

    boolean allowed =
        circleServiceGrpcClient.canUserSendMessage(
            senderUserId,
            request.deviceId()
        );

    if (!allowed) {
      throw new MessageAccessDeniedException(
          "You do not have permission to send messages to this device"
      );
    }

    Message message =
        Message.builder()
            .deviceId(request.deviceId())
            .senderUserId(senderUserId)
            .content(request.content().trim())
            .status(MessageStatus.PENDING)
            .build();

    message =
        messageRepository.save(message);

    MessageCreatedEventDTO event =
        new MessageCreatedEventDTO(
            message.getId(),
            message.getDeviceId(),
            message.getSenderUserId(),
            message.getContent(),
            message.getCreatedAt()
        );

    messageEventPublisher.publish(event);

    return MessageMapper.toResponse(message);
  }

  @Transactional(readOnly = true)
  public List<MessageResponseDTO> getMessages(
      UUID userId,
      UUID deviceId
  ) {

    boolean allowed =
        circleServiceGrpcClient.canUserSendMessage(
            userId,
            deviceId
        );

    if (!allowed) {
      throw new MessageAccessDeniedException(
          "You do not have access to this device"
      );
    }

    return messageRepository
        .findAllByDeviceIdOrderByCreatedAtDesc(deviceId)
        .stream()
        .map(MessageMapper::toResponse)
        .toList();
  }

}
