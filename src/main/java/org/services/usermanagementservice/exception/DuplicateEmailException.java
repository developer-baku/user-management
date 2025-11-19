package org.services.usermanagementservice.exception;

public class DuplicateEmailException extends RuntimeException {
  public DuplicateEmailException(String message) {
    super(message);
  }
}
