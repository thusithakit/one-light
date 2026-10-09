package com.thusithakit.deliveryservice.kafka;

import com.thusithakit.deliveryservice.dto.MessageCreatedEventDTO;
import com.thusithakit.deliveryservice.service.DeviceDeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageCreatedConsumer {

  private static final Logger log =
      LoggerFactory.getLogger(MessageCreatedConsumer.class);

  private final DeviceDeliveryService deliveryService;

  public MessageCreatedConsumer(
      DeviceDeliveryService deliveryService
  ) {
    this.deliveryService = deliveryService;
  }

  @KafkaListener(
      topics = "${onelight.kafka.message-created-topic}",
      groupId = "${spring.kafka.consumer.group-id}"
  )
  public void consume(MessageCreatedEventDTO event) {
    log.info(
        "Received message-created event: messageId={}, deviceId={}",
        event.messageId(),
        event.deviceId()
    );

    deliveryService.deliver(event);
  }
}
