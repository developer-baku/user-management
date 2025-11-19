package org.services.usermanagementservice.dto;

import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Getter
@Setter
public class CommonResponse<T> {

  private boolean success;
  private String message;
  private T data;
  private LocalDateTime timestamp;

  public static <T> CommonResponse<T> success(String message, T data) {
    return new CommonResponse<>(true, message, data, LocalDateTime.now());
  }
}
