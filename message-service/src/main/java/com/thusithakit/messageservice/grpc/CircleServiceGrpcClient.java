package com.thusithakit.messageservice.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import messageservice.CanUserSendMessageRequest;
import messageservice.CanUserSendMessageResponse;
import messageservice.CircleAuthorizationServiceGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CircleServiceGrpcClient {

  private final CircleAuthorizationServiceGrpc
      .CircleAuthorizationServiceBlockingStub stub;

  public CircleServiceGrpcClient(
      @Value("${grpc.circle-service.host}") String host,
      @Value("${grpc.circle-service.port}") int port
  ) {

    ManagedChannel channel =
        ManagedChannelBuilder
            .forAddress(host, port)
            .usePlaintext()
            .build();

    stub = CircleAuthorizationServiceGrpc
        .newBlockingStub(channel);
  }

  public boolean canUserSendMessage(
      UUID userId,
      UUID deviceId
  ) {

    CanUserSendMessageRequest request =
        CanUserSendMessageRequest
            .newBuilder()
            .setUserId(userId.toString())
            .setDeviceId(deviceId.toString())
            .build();

    CanUserSendMessageResponse response =
        stub.canUserSendMessage(request);

    return response.getAllowed();
  }
}
