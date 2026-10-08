package com.thusithakit.messageservice.kafka;

import com.thusithakit.messageservice.dto.MessageCreatedEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageEventPublisher {
  private static final String TOPIC =
      "onelight.message.created";

  private final KafkaTemplate<String, MessageCreatedEventDTO>
      kafkaTemplate;

  public void publish(MessageCreatedEventDTO event) {

    kafkaTemplate.send(
        TOPIC,
        event.deviceId().toString(),
        event
    );
  }
}
