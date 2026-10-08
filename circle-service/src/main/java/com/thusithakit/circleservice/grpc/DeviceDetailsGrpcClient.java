package com.thusithakit.circleservice.grpc;

import deviceservice.DeviceDetailsServiceGrpc;
import deviceservice.GetDeviceDetailsRequest;
import deviceservice.GetDeviceDetailsResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeviceDetailsGrpcClient {

  private final DeviceDetailsServiceGrpc.DeviceDetailsServiceBlockingStub stub;

  public DeviceDetailsGrpcClient(
      @Value("${grpc.device-service.host:localhost}") String host,
      @Value("${grpc.device-service.port:9094}") int port
  ) {
    ManagedChannel channel = ManagedChannelBuilder
        .forAddress(host, port)
        .usePlaintext()
        .build();

    this.stub = DeviceDetailsServiceGrpc
        .newBlockingStub(channel);
  }

  public DeviceDetails getDeviceDetails(UUID deviceId) {

    GetDeviceDetailsRequest request =
        GetDeviceDetailsRequest.newBuilder()
            .setDeviceId(deviceId.toString())
            .build();

    GetDeviceDetailsResponse response =
        stub.getDeviceDetails(request);

    return new DeviceDetails(
        UUID.fromString(response.getDeviceId()),
        response.getName(),
        response.getStatus(),
        response.getFirmwareVersion(),
        response.getLastSeenAt()
    );
  }

  public record DeviceDetails(
      UUID deviceId,
      String name,
      String status,
      String firmwareVersion,
      String lastSeenAt
  ) {
  }
}
