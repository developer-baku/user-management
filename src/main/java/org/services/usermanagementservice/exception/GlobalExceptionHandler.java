package org.services.usermanagementservice.exception;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.services.usermanagementservice.dto.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  private <T> ResponseEntity<CommonResponse<T>> buildError(HttpStatus status, String message) {

    CommonResponse<T> response =
        CommonResponse.<T>builder()
            .success(false)
            .message(message)
            .data(null)
            .timestamp(LocalDateTime.now())
            .build();

    return ResponseEntity.status(status).body(response);
  }

  /* ------------------------------------------------------
     USER NOT FOUND
  ------------------------------------------------------ */
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<CommonResponse<Void>> handleNotFound(UserNotFoundException ex) {
    log.warn("User not found: {}", ex.getMessage());
    return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  /* ------------------------------------------------------
     DUPLICATE EMAIL
  ------------------------------------------------------ */
  @ExceptionHandler(DuplicateEmailException.class)
  public ResponseEntity<CommonResponse<Void>> handleDuplicate(DuplicateEmailException ex) {
    log.warn("Duplicate email: {}", ex.getMessage());
    return buildError(HttpStatus.CONFLICT, ex.getMessage());
  }

  /* ------------------------------------------------------
     VALIDATION ERRORS (@Valid DTO)
  ------------------------------------------------------ */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CommonResponse<Void>> handleValidationError(
      MethodArgumentNotValidException ex) {

    String message =
        ex.getBindingResult().getFieldErrors().stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .findFirst()
            .orElse("Validation error");

    log.warn("Validation error: {}", message);

    return buildError(HttpStatus.BAD_REQUEST, message);
  }

  /* ------------------------------------------------------
     GENERIC EXCEPTIONS
  ------------------------------------------------------ */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<CommonResponse<Void>> handleAll(Exception ex) {
    log.error("Unexpected server error", ex);
    return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
  }
}
