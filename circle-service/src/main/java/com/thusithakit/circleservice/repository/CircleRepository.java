package com.thusithakit.circleservice.repository;

import com.thusithakit.circleservice.model.Circle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CircleRepository extends JpaRepository<Circle, UUID> {

    List<Circle> findAllByOwnerId(UUID ownerId);

    boolean existsByIdAndOwnerId(UUID id, UUID ownerId);
}
