package com.thusithakit.userservice.controller;

import com.thusithakit.userservice.dto.UserResponseDTO;
import com.thusithakit.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{authUserId}")
    public UserResponseDTO getUser(
            @PathVariable UUID authUserId
    ) {
        return userService.getByAuthUserId(authUserId);
    }
}
