package com.thusithakit.deviceservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record LightSettingsRequestDTO(
    Boolean on,

    @Min(0)
    @Max(255)
    Integer brightness,

    @Pattern(
        regexp = "^#[0-9a-fA-F]{6}$",
        message = "Color must use #RRGGBB format"
    )
    String color
) {
  public boolean isEmpty() {
    return on == null
        && brightness == null
        && color == null;
  }
}
