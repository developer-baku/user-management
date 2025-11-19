package org.services.usermanagementservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users") // Always plural for table names
public class User {

  @Id @GeneratedValue private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  private String phone;

  @Column(nullable = false)
  private String role;

  @Column(nullable = false)
  private Boolean active = true;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt = LocalDateTime.now();

  // --- Constructors ---
  public User() {}

  public User(String name, String email, String phone, String role) {
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.role = role;
    this.active = true;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  // --- Getters & Setters ---
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    this.updatedAt = LocalDateTime.now();
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
    this.updatedAt = LocalDateTime.now();
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
    this.updatedAt = LocalDateTime.now();
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
    this.updatedAt = LocalDateTime.now();
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
    this.updatedAt = LocalDateTime.now();
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }
}
