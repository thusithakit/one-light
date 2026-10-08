package com.thusithakit.circleservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CircleNotFoundException.class)
  public ResponseEntity<?> handleNotFound(
      CircleNotFoundException exception
  ) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(Map.of(
            "error", exception.getMessage()
        ));
  }

  @ExceptionHandler(CircleAccessDeniedException.class)
  public ResponseEntity<?> handleAccessDenied(
      CircleAccessDeniedException exception
  ) {
    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(Map.of(
            "error", exception.getMessage()
        ));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handleBadRequest(
      IllegalArgumentException exception
  ) {
    return ResponseEntity
        .badRequest()
        .body(Map.of(
            "error", exception.getMessage()
        ));
  }
}
