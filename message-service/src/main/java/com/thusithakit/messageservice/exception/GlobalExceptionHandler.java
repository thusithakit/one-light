package com.thusithakit.messageservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MessageAccessDeniedException.class)
  public ResponseEntity<?> handleAccessDenied(
      MessageAccessDeniedException exception
  ) {

    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(Map.of(
            "error", "FORBIDDEN",
            "message", exception.getMessage()
        ));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidation(
      MethodArgumentNotValidException exception
  ) {

    return ResponseEntity
        .badRequest()
        .body(Map.of(
            "error", "VALIDATION_ERROR",
            "message", "Invalid request"
        ));
  }
}
