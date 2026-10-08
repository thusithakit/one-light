package com.thusithakit.userservice.controller;

import com.thusithakit.userservice.dto.UpdateUserRequestDTO;
import com.thusithakit.userservice.dto.UserResponseDTO;
import com.thusithakit.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{authUserId}")
    public ResponseEntity<UserResponseDTO> getUser(
            @PathVariable UUID authUserId
    ) {
        UserResponseDTO response = userService.getByAuthUserId(authUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(
            @RequestHeader("X-User-Id") UUID authUserId
    ) {
        UserResponseDTO response = userService.myInfo(authUserId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateCurrentUser(
            @RequestHeader("X-User-Id") UUID authUserId,
            @RequestBody UpdateUserRequestDTO updateUserRequest
            ) {
        UserResponseDTO response = userService.updateUser(authUserId, updateUserRequest);
        return ResponseEntity.ok(response);
    }
}
