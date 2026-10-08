package com.thusithakit.messageservice.controller;

import com.thusithakit.messageservice.dto.MessageResponseDTO;
import com.thusithakit.messageservice.dto.SendMessageRequestDTO;
import com.thusithakit.messageservice.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @PostMapping
  public MessageResponseDTO sendMessage(
      @RequestHeader("X-User-Id") UUID userId,
      @Valid @RequestBody SendMessageRequestDTO request
  ) {

    return messageService.sendMessage(
        userId,
        request
    );
  }

  @GetMapping("/{deviceId}")
  public List<MessageResponseDTO> getMessages(
      @RequestHeader("X-User-Id") UUID userId,
      @PathVariable UUID deviceId
  ) {

    return messageService.getMessages(
        userId,
        deviceId
    );
  }
}
