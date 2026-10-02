package com.thusithakit.userservice.mapper;

import com.thusithakit.userservice.dto.UserResponseDTO;
import com.thusithakit.userservice.model.User;

public class UserMapper {
    public static UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getAuthUserId(),
            user.getName(),
            user.getAvatarUrl(),
            user.getTimezone(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
