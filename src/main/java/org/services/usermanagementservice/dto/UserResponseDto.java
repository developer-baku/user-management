package org.services.usermanagementservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponseDto {
  private UUID id;
  private String name;
  private String email;
  private String phone;
  private String role;
  private Boolean active;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
