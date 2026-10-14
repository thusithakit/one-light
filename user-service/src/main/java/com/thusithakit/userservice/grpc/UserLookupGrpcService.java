package com.thusithakit.userservice.grpc;

import com.thusithakit.userservice.repository.UserRepository;
import io.grpc.Status;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import userservice.GetUserProfileRequest;
import userservice.GetUserProfileResponse;
import userservice.UserLookupServiceGrpc;

import java.util.UUID;

@GrpcService
public class UserLookupGrpcService
    extends UserLookupServiceGrpc.UserLookupServiceImplBase {

  private final UserRepository userRepository;

  private static final Logger log =
      LoggerFactory.getLogger(UserLookupGrpcService.class);

  public UserLookupGrpcService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void getUserProfile(
      GetUserProfileRequest request,
      io.grpc.stub.StreamObserver<GetUserProfileResponse> observer
  ) {
    try {
      UUID userId = UUID.fromString(request.getAuthUserId());

      log.info("user id: {}",userId);

      var user = userRepository.findByAuthUserId(userId);

      if (user.isEmpty()) {
        observer.onError(
            Status.NOT_FOUND
                .withDescription("User not found")
                .asRuntimeException()
        );
        return;
      }

      var entity = user.get();

      // Adapt getDisplayName() to your actual User entity.
      String displayName = entity.getName();

      GetUserProfileResponse response =
          GetUserProfileResponse.newBuilder()
              .setUserId(entity.getId().toString())
              .setDisplayName(displayName == null ? "" : displayName)
              .build();

      observer.onNext(response);
      observer.onCompleted();

    } catch (IllegalArgumentException exception) {
      observer.onError(
          Status.INVALID_ARGUMENT
              .withDescription("Invalid user ID")
              .asRuntimeException()
      );
    }
  }
}
