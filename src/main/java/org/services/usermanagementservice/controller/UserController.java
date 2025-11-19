package org.services.usermanagementservice.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.services.usermanagementservice.dto.CommonResponse;
import org.services.usermanagementservice.dto.UserRequestDto;
import org.services.usermanagementservice.dto.UserResponseDto;
import org.services.usermanagementservice.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /* ------------------------------------------------------
     CREATE USER
  ------------------------------------------------------ */
  @PostMapping
  public ResponseEntity<CommonResponse<UserResponseDto>> createUser(
      @Valid @RequestBody UserRequestDto request) {

    log.info("➡️ [CREATE USER] Request: {}", request);

    UserResponseDto response = userService.createUser(request);

    log.info("✅ [CREATE USER] User created: {}", response.getId());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            CommonResponse.<UserResponseDto>builder()
                .success(true)
                .message("User created successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build());
  }

  /* ------------------------------------------------------
     GET USER BY ID
  ------------------------------------------------------ */
  @GetMapping("/{id}")
  public ResponseEntity<CommonResponse<UserResponseDto>> getUser(@PathVariable UUID id) {

    log.info("➡️ [GET USER] ID: {}", id);

    UserResponseDto response = userService.getUserById(id);

    log.info("✅ [GET USER] Found user: {}", response.getId());

    return ResponseEntity.ok(
        CommonResponse.<UserResponseDto>builder()
            .success(true)
            .message("User retrieved successfully")
            .data(response)
            .timestamp(LocalDateTime.now())
            .build());
  }

  /* ------------------------------------------------------
     GET ALL USERS
  ------------------------------------------------------ */
  @GetMapping("/all")
  public ResponseEntity<CommonResponse<List<UserResponseDto>>> getAllUsers() {

    log.info("➡️ [GET ALL USERS]");

    List<UserResponseDto> users = userService.getAllUsers();

    log.info("✅ [GET ALL USERS] Count={}", users.size());

    return ResponseEntity.ok(
        CommonResponse.<List<UserResponseDto>>builder()
            .success(true)
            .message("Users retrieved successfully")
            .data(users)
            .timestamp(LocalDateTime.now())
            .build());
  }

  /* ------------------------------------------------------
     PAGINATION
  ------------------------------------------------------ */
  @GetMapping
  public ResponseEntity<CommonResponse<Page<UserResponseDto>>> getUsersPage(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

    log.info("➡️ [GET USERS PAGED] page={}, size={}", page, size);

    Page<UserResponseDto> result = userService.getUsersPage(page, size);

    log.info("✅ [GET USERS PAGED] Returned {} users", result.getTotalElements());

    return ResponseEntity.ok(CommonResponse.success("Users fetched successfully", result));
  }

  /* ------------------------------------------------------
     UPDATE USER
  ------------------------------------------------------ */
  @PutMapping("/{id}")
  public ResponseEntity<CommonResponse<UserResponseDto>> updateUser(
      @PathVariable UUID id, @Valid @RequestBody UserRequestDto request) {

    log.info("➡️ [UPDATE USER] ID={}, Request={}", id, request);

    UserResponseDto updated = userService.updateUser(id, request);

    log.info("✅ [UPDATE USER] Updated user: {}", id);

    return ResponseEntity.ok(
        CommonResponse.<UserResponseDto>builder()
            .success(true)
            .message("User updated successfully")
            .data(updated)
            .timestamp(LocalDateTime.now())
            .build());
  }

  /* ------------------------------------------------------
     DELETE USER
  ------------------------------------------------------ */
  @DeleteMapping("/{id}")
  public ResponseEntity<CommonResponse<Void>> deleteUser(@PathVariable UUID id) {

    log.info("➡️ [DELETE USER] ID={}", id);

    userService.deleteUser(id);

    log.info("🗑️ [DELETE USER] Deleted user: {}", id);

    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .body(
            CommonResponse.<Void>builder()
                .success(true)
                .message("User deleted successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
  }
}
