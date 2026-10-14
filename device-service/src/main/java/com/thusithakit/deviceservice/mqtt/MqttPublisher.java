package com.thusithakit.deviceservice.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class MqttPublisher {

  private final String brokerUrl;
  private final String username;
  private final String password;

  private MqttClient client;

  private static final Logger log =
      LoggerFactory.getLogger(MqttPublisher.class);

  public MqttPublisher(
      @Value("${onelight.mqtt.broker-uri}") String brokerUri,
      @Value("${onelight.mqtt.username}") String username,
      @Value("${onelight.mqtt.password}") String password
  ) {
    this.brokerUrl = brokerUri;
    this.username = username;
    this.password = password;
  }

  private synchronized MqttClient getClient()
      throws MqttException {

    if (client != null && client.isConnected()) {
      return client;
    }

    client = new MqttClient(
        brokerUrl,
        "onelight-device-service-" + UUID.randomUUID(),
        new MemoryPersistence()
    );

    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    options.setConnectionTimeout(5);
    options.setKeepAliveInterval(30);

    if (username != null && !username.isBlank()) {
      options.setUserName(username);
      options.setPassword(
          password == null
              ? new char[0]
              : password.toCharArray()
      );
    }

    client.connect(options);
    log.info("Connected to OneLight MQTT broker");
    return client;
  }

  public boolean publishLightCommand(
      UUID deviceId,
      String jsonPayload
  ) {
    String topic =
        "onelight/devices/" + deviceId + "/commands";

    try {
      MqttMessage message = new MqttMessage(
          jsonPayload.getBytes(StandardCharsets.UTF_8)
      );

      message.setQos(1);
      message.setRetained(false);

      getClient().publish(topic, message);
      log.info(
          "Published notification to MQTT topic {} for messageId={}",
          topic,
          message
      );
      return true;

    } catch (MqttException exception) {
      System.err.println(
          "Failed to publish light command for "
              + deviceId + ": " + exception.getMessage()
      );
      return false;
    }
  }
}