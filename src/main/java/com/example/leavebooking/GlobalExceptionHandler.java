package com.example.leavebooking;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ConstraintViolation;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

// Handles exceptions thrown anywhere in the application's controllers.

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleAllExceptions(
      Exception ex) {

    // Default to an internal server error unless we recognise
    // the exception as something more specific.
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    // Use the exception message in the response.
    String message = ex.getMessage();

    // Used when validation produces errors for individual fields.
    Map<String, String> validationErrors = null;

    // Handle exceptions which already contain an HTTP status.
    if (ex instanceof ResponseStatusException rse) {

      status = HttpStatus.valueOf(
          rse.getStatusCode().value());

      message = rse.getReason();
    }

    // Handle validation errors from incoming request objects.
    else if (ex instanceof MethodArgumentNotValidException manve) {

      status = HttpStatus.BAD_REQUEST;

      message = "Validation failed for one or more fields.";

      validationErrors = manve
          .getBindingResult()
          .getFieldErrors()
          .stream()
          .collect(
              Collectors.toMap(
                  FieldError::getField,
                  error -> Objects.requireNonNullElse(
                      error.getDefaultMessage(),
                      "Invalid value"),

                  // If two errors exist for the same field keep the first one.
                  (existing, replacement) -> existing));
    }

    else if (ex instanceof ConstraintViolationException cve) {

      status = HttpStatus.BAD_REQUEST;

      message = "Database constraint validation failed.";

      validationErrors = cve
          .getConstraintViolations()
          .stream()
          .collect(
              Collectors.toMap(
                  violation -> violation
                      .getPropertyPath()
                      .toString(),

                  ConstraintViolation::getMessage));
    }

    else if (ex instanceof DataIntegrityViolationException) {
      status = HttpStatus.BAD_REQUEST;
      message = "A duplicate record already exists";
    }

    else if (ex instanceof IllegalArgumentException) {
      status = HttpStatus.BAD_REQUEST;
      message = ex.getMessage();
    }

    Map<String, Object> responseBody = new java.util.HashMap<>(
        Map.of(
            "status",
            status.value(),

            "error",
            status.getReasonPhrase(),

            "message",
            Objects.requireNonNullElse(
                message,
                "No message provided"),

            "timestamp",
            Instant.now().toString()));

    // Only add field-level validation errors when they exist.
    if (validationErrors != null) {
      responseBody.put(
          "errors",
          validationErrors);
    }

    return ResponseEntity
        .status(status)
        .body(responseBody);
  }
}