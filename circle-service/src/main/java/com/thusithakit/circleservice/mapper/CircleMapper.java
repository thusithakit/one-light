package com.thusithakit.circleservice.mapper;

import com.thusithakit.circleservice.dto.CircleResponseDTO;
import com.thusithakit.circleservice.model.Circle;

public class CircleMapper {
  public static CircleResponseDTO toCircleResponse(
      Circle circle
  ) {

    return new CircleResponseDTO(
        circle.getId(),
        circle.getName(),
        circle.getOwnerId(),
        circle.getCreatedAt(),
        circle.getUpdatedAt()
    );
  }
}
