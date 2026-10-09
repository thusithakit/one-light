package com.thusithakit.messageservice.kafka;

import com.thusithakit.messageservice.dto.MessageCreatedEventDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageEventPublisher {
  private static final String TOPIC =
      "onelight.message.created";

  private final KafkaTemplate<String, MessageCreatedEventDTO>
      kafkaTemplate;

  private static final Logger log =
      LoggerFactory.getLogger(MessageEventPublisher.class);

  public void publish(MessageCreatedEventDTO event) {

    kafkaTemplate.send(
        TOPIC,
        event.deviceId().toString(),
        event
    ).whenComplete((result, exception) -> {
      if (exception != null) {
        log.error("Failed to publish message-created event", exception);
      } else {
        log.info(
            "Published message-created event to topic={}, partition={}, offset={}",
            result.getRecordMetadata().topic(),
            result.getRecordMetadata().partition(),
            result.getRecordMetadata().offset()
        );
      }
    });
  }
}
