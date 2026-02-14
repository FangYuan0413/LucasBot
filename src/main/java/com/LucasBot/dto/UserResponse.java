package com.LucasBot.dto;

import com.LucasBot.entity.User;

import java.time.LocalDateTime;

public class UserResponse {
    private Long id;
    private String username;
    private String phoneNumber;
    private String email;
    private String role;
    private boolean emailVerified;
    private LocalDateTime createdAt;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.emailVerified = user.isEmailVerified();
        this.createdAt = user.getCreatedAt();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public boolean isEmailVerified() { return emailVerified; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
