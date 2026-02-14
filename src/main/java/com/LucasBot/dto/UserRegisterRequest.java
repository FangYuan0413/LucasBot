package com.LucasBot.dto;

public class UserRegisterRequest {
    private String username;
    private String phoneNumber;
    private String email;
    private String password;

    public UserRegisterRequest() {}

    public String getUsername() { return username; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setUsername(String username) { this.username = username; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}
