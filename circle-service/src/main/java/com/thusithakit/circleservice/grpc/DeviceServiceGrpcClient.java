package com.thusithakit.circleservice.grpc;

import deviceservice.DeviceAuthorizationServiceGrpc;
import deviceservice.VerifyDeviceOwnershipRequest;
import deviceservice.VerifyDeviceOwnershipResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeviceServiceGrpcClient {

  private final DeviceAuthorizationServiceGrpc
      .DeviceAuthorizationServiceBlockingStub stub;

  public DeviceServiceGrpcClient(
      @Value("${grpc.device-service.host}")
      String host,

      @Value("${grpc.device-service.port}")
      int port
  ) {

    ManagedChannel channel =
        ManagedChannelBuilder
            .forAddress(host, port)
            .usePlaintext()
            .build();

    stub = DeviceAuthorizationServiceGrpc
        .newBlockingStub(channel);
  }

  public boolean verifyDeviceOwnership(
      UUID userId,
      UUID deviceId
  ) {

    VerifyDeviceOwnershipRequest request =
        VerifyDeviceOwnershipRequest.newBuilder()
            .setUserId(userId.toString())
            .setDeviceId(deviceId.toString())
            .build();

    VerifyDeviceOwnershipResponse response =
        stub.verifyDeviceOwnership(request);

    return response.getOwned();
  }
}
