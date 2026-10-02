package com.thusithakit.authservice.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import userservice.CreateUserProfileRequest;
import userservice.CreateUserProfileResponse;
import userservice.UserProfileServiceGrpc;

import java.util.UUID;

@Component
public class UserServiceGrpcClient {
    private final UserProfileServiceGrpc.UserProfileServiceBlockingStub stub;

    public UserServiceGrpcClient(
            @Value("${grpc.user-service.host}") String host,
            @Value("${grpc.user-service.port}") int port
    ) {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();

        stub = UserProfileServiceGrpc
                .newBlockingStub(channel);
    }

    public CreateUserProfileResponse createUserProfile(
            UUID authUserId,
            String name,
            String timezone
    ) {

        CreateUserProfileRequest request =
                CreateUserProfileRequest.newBuilder()
                    .setAuthUserId(authUserId.toString())
                    .setName(name)
                    .setTimezone(timezone)
                    .build();

        return stub.createUserProfile(request);
    }
}
