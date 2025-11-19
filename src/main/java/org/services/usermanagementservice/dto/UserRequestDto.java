package org.services.usermanagementservice.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRequestDto {
  @NotBlank(message = "Name is required")
  @Size(max = 255)
  private String name;

  @NotBlank(message = "Email is required")
  @Email(message = "Email must be valid")
  @Size(max = 255)
  private String email;

  @Size(max = 50)
  private String phone;

  @NotBlank(message = "Role is required")
  @Size(max = 50)
  private String role;

  @Column(nullable = false)
  private Boolean active = true;
}
