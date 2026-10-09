package com.thusithakit.deliveryservice.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thusithakit.deliveryservice.dto.MessageCreatedEventDTO;
import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class DeviceMqttPublisher {

  private static final Logger log =
      LoggerFactory.getLogger(DeviceMqttPublisher.class);

  private final ObjectMapper objectMapper;
  private final MqttClient mqttClient;
  private final String username;
  private final String password;

  public DeviceMqttPublisher(
      ObjectMapper objectMapper,
      @Value("${onelight.mqtt.broker-uri}") String brokerUri,
      @Value("${onelight.mqtt.username}") String username,
      @Value("${onelight.mqtt.password}") String password
  ) throws MqttException {
    this.objectMapper = objectMapper;
    this.username = username;
    this.password = password;

    this.mqttClient = new MqttClient(
        brokerUri,
        "onelight-notification-" + UUID.randomUUID(),
        new MemoryPersistence()
    );

    connect();
  }

  private synchronized void connect() throws MqttException {
    if (mqttClient.isConnected()) {
      return;
    }

    MqttConnectOptions options = new MqttConnectOptions();
    options.setUserName(username);
    options.setPassword(password.toCharArray());
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    options.setConnectionTimeout(10);
    options.setKeepAliveInterval(30);

    mqttClient.connect(options);

    log.info("Connected to OneLight MQTT broker");
  }

  public void publishMessage(
      String deviceId,
      MessageCreatedEventDTO event
  ) throws MqttException, JsonProcessingException {

    if (deviceId == null || deviceId.isBlank()) {
      throw new IllegalArgumentException("deviceId must not be blank");
    }

    if (event.content() == null || event.content().isBlank()) {
      throw new IllegalArgumentException("Message content must not be blank");
    }

    if (!mqttClient.isConnected()) {
      connect();
    }

    String topic = "onelight/devices/" + deviceId + "/messages";

    String payload = objectMapper.writeValueAsString(event);

    MqttMessage mqttMessage = new MqttMessage(
        payload.getBytes(StandardCharsets.UTF_8)
    );

    mqttMessage.setQos(1);
    mqttMessage.setRetained(false);

    mqttClient.publish(topic, mqttMessage);

    log.info(
        "Published notification to MQTT topic {} for messageId={}",
        topic,
        event.messageId()
    );
  }

  @PreDestroy
  public void disconnect() {
    try {
      if (mqttClient.isConnected()) {
        mqttClient.disconnect();
      }

      mqttClient.close();
    } catch (MqttException exception) {
      log.warn("Error closing MQTT client", exception);
    }
  }
}
