package com.thusithakit.userservice.service;

import com.thusithakit.userservice.dto.UpdateUserRequestDTO;
import com.thusithakit.userservice.dto.UserResponseDTO;
import com.thusithakit.userservice.exception.UserNotFoundException;
import com.thusithakit.userservice.mapper.UserMapper;
import com.thusithakit.userservice.model.User;
import com.thusithakit.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponseDTO createUser(
            UUID authUserId,
            String name,
            String timezone
    ) {
        // Idempotency
        var existingUser = userRepository.findByAuthUserId(authUserId);

        if (existingUser.isPresent()) {
            return UserMapper.toResponse(existingUser.get());
        }

        User user = User.builder()
                .authUserId(authUserId)
                .name(name)
                .timezone(timezone)
                .build();

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getByAuthUserId(UUID authUserId) {

        User user = userRepository
                .findByAuthUserId(authUserId)
                .orElseThrow(
                        () -> new UserNotFoundException(authUserId)
                );

        return UserMapper.toResponse(user);
    }

    @Transactional
    public UserResponseDTO updateUser(
            UUID authUserId,
            UpdateUserRequestDTO request
    ) {

        User user = userRepository
                .findByAuthUserId(authUserId)
                .orElseThrow(
                        () -> new UserNotFoundException(authUserId)
                );

        if (request.name() != null) {
            user.setName(request.name());
        }

        if (request.avatarUrl() != null) {
            user.setAvatarUrl(request.avatarUrl());
        }

        if (request.timezone() != null) {
            user.setTimezone(request.timezone());
        }

        return UserMapper.toResponse(user);
    }

    public UserResponseDTO myInfo(
            UUID authUserId
    ){
        User user = userRepository
                .findByAuthUserId(authUserId)
                .orElseThrow(() ->
                        new RuntimeException("User profile not found")
                );
        return UserMapper.toResponse(user);
    }
}
