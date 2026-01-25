package com.LucasBot.dto;


import com.LucasBot.entity.User;

public class UserResponse {
    private Long id;
    private String username;
    private String phoneNumber;
    private String email;

    public UserResponse() {
    }

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
}

