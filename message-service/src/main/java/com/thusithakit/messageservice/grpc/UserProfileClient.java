package com.thusithakit.messageservice.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import userservice.GetUserProfileRequest;
import userservice.UserLookupServiceGrpc;

import java.util.UUID;

@Component
public class UserProfileClient {

  private final UserLookupServiceGrpc.UserLookupServiceBlockingStub stub;

  public UserProfileClient(
      @Value("${grpc.user-service.host}") String host,
      @Value("${grpc.user-service.port}") int port
  ) {
    // Internal development network only
    ManagedChannel channel = ManagedChannelBuilder
        .forAddress(host, port)
        .usePlaintext() // Internal development network only
        .build();

    this.stub = UserLookupServiceGrpc
        .newBlockingStub(channel);
  }

  public String getDisplayName(UUID userId) {
    var response = stub.getUserProfile(
        GetUserProfileRequest.newBuilder()
            .setAuthUserId(userId.toString())
            .build()
    );

    String name = response.getDisplayName().trim();

    return name.isEmpty() ? "Unknown sender" : name;
  }
}
