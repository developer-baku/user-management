package org.services.usermanagementservice.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.services.usermanagementservice.dto.UserRequestDto;
import org.services.usermanagementservice.dto.UserResponseDto;
import org.services.usermanagementservice.entity.User;
import org.services.usermanagementservice.exception.DuplicateEmailException;
import org.services.usermanagementservice.exception.UserNotFoundException;
import org.services.usermanagementservice.repository.UserRepository;
import org.springframework.data.domain.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;


  @InjectMocks private UserService userService;

  // ---------------------------------------------------------
  // CREATE USER
  // ---------------------------------------------------------
  @Test
  void createUser_success() {

    UserRequestDto dto = new UserRequestDto("John", "john@test.com", "12345", "ADMIN", true);

    User saved = new User("John", "john@test.com", "12345", "ADMIN");
    saved.setId(UUID.randomUUID());

    when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

    when(userRepository.save(any(User.class))).thenReturn(saved);

    UserResponseDto result = userService.createUser(dto);

    assertThat(result.getEmail()).isEqualTo(dto.getEmail());

    verify(userRepository).save(any(User.class));
  }

  @Test
  void createUser_duplicateEmail_throwsException() {
    UserRequestDto dto = new UserRequestDto("John", "john@test.com", "12345", "ADMIN", true);

    when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new User()));

    assertThatThrownBy(() -> userService.createUser(dto))
        .isInstanceOf(DuplicateEmailException.class);
  }

  // ---------------------------------------------------------
  // GET USER BY ID
  // ---------------------------------------------------------
  @Test
  void getUserById_success() {
    UUID id = UUID.randomUUID();
    User user = new User("A", "a@test.com", "123", "ADMIN");
    user.setId(id);

    when(userRepository.findById(id)).thenReturn(Optional.of(user));

    UserResponseDto dto = userService.getUserById(id);

    assertThat(dto.getId()).isEqualTo(id);
  }

  @Test
  void getUserById_notFound() {
    UUID id = UUID.randomUUID();

    when(userRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.getUserById(id)).isInstanceOf(UserNotFoundException.class);
  }

  // ---------------------------------------------------------
  // UPDATE USER
  // ---------------------------------------------------------
  @Test
  void updateUser_success() {

    UUID id = UUID.randomUUID();

    UserRequestDto dto = new UserRequestDto("New", "new@test.com", "444", "USER", true);

    User existing = new User("Old", "old@test.com", "333", "ADMIN");
    existing.setId(id);

    when(userRepository.findById(id)).thenReturn(Optional.of(existing));

    when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

    when(userRepository.save(any(User.class))).thenReturn(existing);

    UserResponseDto result = userService.updateUser(id, dto);

    assertThat(result.getEmail()).isEqualTo("new@test.com");
  }

  @Test
  void updateUser_userNotFound() {
    UUID id = UUID.randomUUID();

    UserRequestDto dto = new UserRequestDto("A", "a@test.com", "1", "U", true);

    when(userRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.updateUser(id, dto))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void updateUser_duplicateEmail() {
    UUID id = UUID.randomUUID();

    UserRequestDto dto = new UserRequestDto("A", "new@test.com", "1", "U", true);

    User existing = new User("Old", "old@test.com", "333", "ADMIN");
    existing.setId(id);

    when(userRepository.findById(id)).thenReturn(Optional.of(existing));
    when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new User()));

    assertThatThrownBy(() -> userService.updateUser(id, dto))
        .isInstanceOf(DuplicateEmailException.class);
  }

  // ---------------------------------------------------------
  // DELETE USER
  // ---------------------------------------------------------
  @Test
  void deleteUser_success() {

    UUID id = UUID.randomUUID();

    when(userRepository.existsById(id)).thenReturn(true);

    userService.deleteUser(id);

    verify(userRepository).deleteById(id);
  }

  @Test
  void deleteUser_notFound() {
    UUID id = UUID.randomUUID();

    when(userRepository.existsById(id)).thenReturn(false);

    assertThatThrownBy(() -> userService.deleteUser(id)).isInstanceOf(UserNotFoundException.class);
  }

  // ---------------------------------------------------------
  // PAGINATION
  // ---------------------------------------------------------
  @Test
  void getUsersPage_success() {

    User u = new User("A", "a@a.com", "123", "USER");
    u.setId(UUID.randomUUID());

    Page<User> page = new PageImpl<>(List.of(u));

    when(userRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

    Page<UserResponseDto> result = userService.getUsersPage(0, 10);

    assertThat(result.getTotalElements()).isEqualTo(1);
  }
}
