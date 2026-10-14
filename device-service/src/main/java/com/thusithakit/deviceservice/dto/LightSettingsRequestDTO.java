package com.thusithakit.deviceservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.openapitools.jackson.nullable.JsonNullable;

public record LightSettingsRequestDTO(
    Boolean on,

    @Min(0)
    @Max(255)
    JsonNullable<Integer> brightness,

    @Pattern(
        regexp = "^#[0-9a-fA-F]{6}$",
        message = "Color must use #RRGGBB format"
    )
    JsonNullable<String> color
) {
  public boolean isEmpty() {
    return on == null
        && (brightness == null || !brightness.isPresent())
        && (color == null || !color.isPresent());
  }
}
