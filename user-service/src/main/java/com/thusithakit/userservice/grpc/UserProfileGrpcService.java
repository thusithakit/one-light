package com.thusithakit.userservice.grpc;

import com.thusithakit.userservice.dto.UserResponseDTO;
import com.thusithakit.userservice.service.UserService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import userservice.CreateUserProfileRequest;
import userservice.CreateUserProfileResponse;
import userservice.UserProfileServiceGrpc;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class UserProfileGrpcService
        extends UserProfileServiceGrpc.UserProfileServiceImplBase {

    private final UserService userService;

    @Override
    public void createUserProfile(
            CreateUserProfileRequest request,
            StreamObserver<CreateUserProfileResponse> responseObserver
    ) {

        try {

            UUID authUserId =
                    UUID.fromString(request.getAuthUserId());

            UserResponseDTO user =
                    userService.createUser(
                            authUserId,
                            request.getName(),
                            request.getTimezone()
                    );

            CreateUserProfileResponse response =
                    CreateUserProfileResponse.newBuilder()
                            .setUserId(user.id().toString())
                            .setAuthUserId(
                                    user.authUserId().toString()
                            )
                            .setName(user.name())
                            .setTimezone(user.timezone())
                            .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {

            responseObserver.onError(e);
        }
    }
}
