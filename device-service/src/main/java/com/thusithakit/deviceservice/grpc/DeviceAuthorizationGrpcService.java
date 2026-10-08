package com.thusithakit.deviceservice.grpc;

import com.thusithakit.deviceservice.repository.DeviceRepository;
import deviceservice.DeviceAuthorizationServiceGrpc;
import deviceservice.VerifyDeviceOwnershipRequest;
import deviceservice.VerifyDeviceOwnershipResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
public class DeviceAuthorizationGrpcService
    extends DeviceAuthorizationServiceGrpc.DeviceAuthorizationServiceImplBase {

  private final DeviceRepository deviceRepository;

  public DeviceAuthorizationGrpcService(
      DeviceRepository deviceRepository
  ) {
    this.deviceRepository = deviceRepository;
  }

  @Override
  public void verifyDeviceOwnership(
      VerifyDeviceOwnershipRequest request,
      StreamObserver<VerifyDeviceOwnershipResponse> responseObserver
  ) {

    UUID userId;
    UUID deviceId;

    try {
      userId = UUID.fromString(request.getUserId());
      deviceId = UUID.fromString(request.getDeviceId());
    } catch (IllegalArgumentException e) {

      responseObserver.onNext(
          VerifyDeviceOwnershipResponse.newBuilder()
              .setOwned(false)
              .build()
      );

      responseObserver.onCompleted();
      return;
    }

    boolean owned = deviceRepository
        .findByIdAndOwnerId(deviceId, userId)
        .isPresent();

    responseObserver.onNext(
        VerifyDeviceOwnershipResponse.newBuilder()
            .setOwned(owned)
            .build()
    );

    responseObserver.onCompleted();
  }
}
