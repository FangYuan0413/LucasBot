package com.LucasBot.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_users_username", columnList = "username", unique = true),
                @Index(name = "idx_users_email", columnList = "email", unique = true)
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // username: required + unique
    @Column(nullable = false, unique = true, length = 30)
    private String username;

    // store hashed password only
    @Column(nullable = false, length = 100)
    private String passwordHash;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false, length = 20)
    private String role = "USER"; // USER / MOD / ADMIN

    @Column(nullable = false)
    private boolean emailVerified = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt;

    protected User() {}

    public User(String username, String phoneNumber, String email, String passwordHash) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.role == null) this.role = "USER";
    }

    // ===== Getters =====
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; } // 不要在 response 里返回它
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRole() { return role; }
    public boolean isEmailVerified() { return emailVerified; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }

    // ===== Setters (minimal) =====
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }
    public void setRole(String role) { this.role = role; }
}
