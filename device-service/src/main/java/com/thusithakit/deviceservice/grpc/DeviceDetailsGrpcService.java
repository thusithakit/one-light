package com.thusithakit.deviceservice.grpc;

import com.thusithakit.deviceservice.model.Device;
import com.thusithakit.deviceservice.repository.DeviceRepository;
import deviceservice.DeviceDetailsServiceGrpc;
import deviceservice.GetDeviceDetailsRequest;
import deviceservice.GetDeviceDetailsResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
public class DeviceDetailsGrpcService
    extends DeviceDetailsServiceGrpc.DeviceDetailsServiceImplBase {

  private final DeviceRepository deviceRepository;

  public DeviceDetailsGrpcService(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
  }

  @Override
  public void getDeviceDetails(
      GetDeviceDetailsRequest request,
      StreamObserver<GetDeviceDetailsResponse> responseObserver
  ) {
    try {
      UUID deviceId = UUID.fromString(request.getDeviceId());

      Device device = deviceRepository.findById(deviceId)
          .orElseThrow(() ->
              Status.NOT_FOUND
                  .withDescription("Device not found")
                  .asRuntimeException()
          );

      GetDeviceDetailsResponse response =
          GetDeviceDetailsResponse.newBuilder()
              .setDeviceId(device.getId().toString())
              .setName(device.getName())
              .setStatus(device.getStatus().name())
              .setFirmwareVersion(
                  device.getFirmwareVersion() != null
                      ? device.getFirmwareVersion()
                      : ""
              )
              .setLastSeenAt(
                  device.getLastSeenAt() != null
                      ? device.getLastSeenAt().toString()
                      : ""
              )
              .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (IllegalArgumentException e) {
      responseObserver.onError(
          Status.INVALID_ARGUMENT
              .withDescription("Invalid device ID")
              .asRuntimeException()
      );
    } catch (RuntimeException e) {
      responseObserver.onError(e);
    }
  }
}
