package com.thusithakit.deliveryservice.service;

import com.thusithakit.deliveryservice.dto.MessageCreatedEventDTO;
import com.thusithakit.deliveryservice.model.DeviceDelivery;
import com.thusithakit.deliveryservice.mqtt.DeviceMqttPublisher;
import com.thusithakit.deliveryservice.repository.DeviceDeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class DeviceDeliveryService {

  private static final Logger log =
      LoggerFactory.getLogger(DeviceDeliveryService.class);

  private final DeviceDeliveryRepository deliveryRepository;
  private final DeviceMqttPublisher mqttPublisher;

  public DeviceDeliveryService(
      DeviceDeliveryRepository deliveryRepository,
      DeviceMqttPublisher mqttPublisher
  ) {
    this.deliveryRepository = deliveryRepository;
    this.mqttPublisher = mqttPublisher;
  }

  public void deliver(MessageCreatedEventDTO event) {
    validateEvent(event);

    DeviceDelivery delivery = getOrCreateDelivery(event);

    if ("PUBLISHED".equals(delivery.getStatus())) {
      log.info(
          "Skipping duplicate published message: {}",
          event.messageId()
      );
      return;
    }

    try {
      delivery.setStatus("PENDING");
      delivery.setFailureReason(null);
      deliveryRepository.save(delivery);

      mqttPublisher.publishMessage(event.deviceId(), event);

      delivery.setStatus("PUBLISHED");
      delivery.setPublishedAt(Instant.now());
      delivery.setFailureReason(null);
      deliveryRepository.save(delivery);

      log.info(
          "Notification published: messageId={}, deviceId={}",
          event.messageId(),
          event.deviceId()
      );
    } catch (Exception exception) {
      delivery.setStatus("FAILED");

      String reason = exception.getMessage();
      delivery.setFailureReason(
          reason == null
              ? exception.getClass().getSimpleName()
              : reason.substring(0, Math.min(reason.length(), 1000))
      );

      deliveryRepository.save(delivery);

      log.error(
          "Notification publication failed: messageId={}",
          event.messageId(),
          exception
      );

      throw new IllegalStateException(
          "Failed to publish notification " + event.messageId(),
          exception
      );
    }
  }

  private DeviceDelivery getOrCreateDelivery(
      MessageCreatedEventDTO event
  ) {
    return deliveryRepository.findByMessageId(event.messageId())
        .orElseGet(() -> {
          DeviceDelivery delivery = new DeviceDelivery();
          delivery.setMessageId(event.messageId());
          delivery.setDeviceId(event.deviceId());
          delivery.setStatus("PENDING");
          delivery.setCreatedAt(Instant.now());

          try {
            return deliveryRepository.saveAndFlush(delivery);
          } catch (DataIntegrityViolationException exception) {
            // A concurrent consumer may have inserted this
            // message's delivery record first.
            return deliveryRepository.findByMessageId(event.messageId())
                .orElseThrow(() -> exception);
          }
        });
  }

  private void validateEvent(MessageCreatedEventDTO event) {
    if (event == null) {
      throw new IllegalArgumentException("Message event must not be null");
    }

    if (event.messageId() == null || event.messageId().isBlank()) {
      throw new IllegalArgumentException("messageId must not be blank");
    }

    if (event.deviceId() == null || event.deviceId().isBlank()) {
      throw new IllegalArgumentException("deviceId must not be blank");
    }

    if (event.content() == null || event.content().isBlank()) {
      throw new IllegalArgumentException("content must not be blank");
    }
  }
}
