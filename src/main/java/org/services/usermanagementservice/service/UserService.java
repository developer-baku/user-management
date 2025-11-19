package org.services.usermanagementservice.service;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.services.usermanagementservice.dto.UserRequestDto;
import org.services.usermanagementservice.dto.UserResponseDto;
import org.services.usermanagementservice.entity.User;
import org.services.usermanagementservice.exception.DuplicateEmailException;
import org.services.usermanagementservice.exception.UserNotFoundException;
import org.services.usermanagementservice.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    // ---------------------------------------------------------
    // CREATE USER
    // ---------------------------------------------------------
    public UserResponseDto createUser(UserRequestDto dto) {

        log.info("➡️ Creating user with email={}", dto.getEmail());

        userRepository
                .findByEmail(dto.getEmail())
                .ifPresent(u -> {
                    log.warn("❌ Email already exists: {}", dto.getEmail());
                    throw new DuplicateEmailException("Email already registered");
                });

        User user = new User(dto.getName(), dto.getEmail(), dto.getPhone(), dto.getRole());

        if (dto.getActive() != null) user.setActive(dto.getActive());

        User saved = userRepository.save(user);

        log.info("✅ User created: id={}, email={}", saved.getId(), saved.getEmail());

        return toResponseDto(saved);
    }

    // ---------------------------------------------------------
    // GET USER BY ID
    // ---------------------------------------------------------
    public UserResponseDto getUserById(UUID id) {

        log.info("➡️ Retrieving user with id={}", id);

        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> {
                            log.warn("❌ User not found: id={}", id);
                            return new UserNotFoundException("User not found");
                        });

        log.info("✅ User retrieved: id={}", id);

        return toResponseDto(user);
    }

    // ---------------------------------------------------------
    // GET ALL USERS
    // ---------------------------------------------------------
    public List<UserResponseDto> getAllUsers() {
        log.info("➡️ Retrieving all users");
        List<UserResponseDto> result =
                userRepository.findAll().stream().map(this::toResponseDto).toList();
        log.info("✅ Retrieved {} users", result.size());
        return result;
    }

    // ---------------------------------------------------------
    // UPDATE USER
    // ---------------------------------------------------------
    public UserResponseDto updateUser(UUID id, UserRequestDto dto) {

        log.info("➡️ Updating user id={}", id);

        User existing =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> {
                            log.warn("❌ Cannot update — user not found: id={}", id);
                            return new UserNotFoundException("User not found");
                        });

        if (!existing.getEmail().equals(dto.getEmail())) {
            userRepository
                    .findByEmail(dto.getEmail())
                    .ifPresent(u -> {
                        log.warn("❌ Cannot update — duplicate email={}", dto.getEmail());
                        throw new DuplicateEmailException("Email already registered");
                    });
        }

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setRole(dto.getRole());

        if (dto.getActive() != null) existing.setActive(dto.getActive());

        User updated = userRepository.save(existing);

        log.info("✅ User updated: id={}, email={}", updated.getId(), updated.getEmail());

        return toResponseDto(updated);
    }

    // ---------------------------------------------------------
    // DELETE USER
    // ---------------------------------------------------------
    public void deleteUser(UUID id) {

        log.info("➡️ Deleting user id={}", id);

        if (!userRepository.existsById(id)) {
            log.warn("❌ Cannot delete — user not found: id={}", id);
            throw new UserNotFoundException("User not found");
        }

        userRepository.deleteById(id);

        log.info("🗑️ User deleted: id={}", id);
    }

    // ---------------------------------------------------------
    // PAGINATION
    // ---------------------------------------------------------
    public Page<UserResponseDto> getUsersPage(int page, int size) {
        log.info("➡️ Fetching users page={}, size={}", page, size);

        Page<User> result = userRepository.findAll(PageRequest.of(page, size));

        log.info("📄 Page fetched: totalElements={}", result.getTotalElements());

        return result.map(this::toResponseDto);
    }

    // DTO mapping
    private UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getActive(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}