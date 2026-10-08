package com.thusithakit.circleservice.grpc;

import circleservice.CanUserSendMessageRequest;
import circleservice.CanUserSendMessageResponse;
import circleservice.CircleAuthorizationServiceGrpc;
import com.thusithakit.circleservice.service.CircleManagementService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
public class CircleAuthorizationGrpcService
    extends CircleAuthorizationServiceGrpc.CircleAuthorizationServiceImplBase {

  private final CircleManagementService circleManagementService;

  public CircleAuthorizationGrpcService(
      CircleManagementService circleManagementService
  ) {
    this.circleManagementService = circleManagementService;
  }

  @Override
  public void canUserSendMessage(
      CanUserSendMessageRequest request,
      StreamObserver<CanUserSendMessageResponse> responseObserver
  ) {

    UUID userId = UUID.fromString(request.getUserId());
    UUID deviceId = UUID.fromString(request.getDeviceId());

    boolean allowed =
        circleManagementService.canUserSendMessage(
            userId,
            deviceId
        );

    CanUserSendMessageResponse response =
        CanUserSendMessageResponse.newBuilder()
            .setAllowed(allowed)
            .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
