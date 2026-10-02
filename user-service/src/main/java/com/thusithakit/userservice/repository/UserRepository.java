package com.thusithakit.userservice.repository;

import com.thusithakit.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByAuthUserId(UUID authUserId);
    boolean existsByAuthUserId(UUID authUserId);
}
