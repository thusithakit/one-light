package com.thusithakit.userservice.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID userId) {
        String message = String.format("A user with user Id %s, doesn't exist", userId.toString());
        super(message);
    }
}
