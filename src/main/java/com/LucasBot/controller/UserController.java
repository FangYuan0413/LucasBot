package com.LucasBot.controller;

import com.LucasBot.dto.UserRegisterRequest;
import com.LucasBot.dto.UserResponse;
import com.LucasBot.entity.User;
import com.LucasBot.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    // Spring Boot style: constructor injection
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // =========================
    // Helpers (Java 8 safe)
    // =========================
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty(); // Java 8 safe "blank" check
    }

    private String requireTrimmed(String value, String fieldName) {
        if (isBlank(value)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " is required");
        }
        return value.trim();
    }

    // =========================
    // Endpoints
    // =========================

    // Register a new user
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody UserRegisterRequest req) {

        // Required fields
        String username = requireTrimmed(req.getUsername(), "username");
        String phoneNumber = requireTrimmed(req.getPhoneNumber(), "phoneNumber");
        String email = requireTrimmed(req.getEmail(), "email");

        if (isBlank(req.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password is required");
        }
        String password = req.getPassword(); // usually don't trim passwords

        // Uniqueness checks (use trimmed values)
        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already exists");
        }

        // Save
        User user = new User(username, phoneNumber, email, password);
        User saved = userRepository.save(user);

        return new UserResponse(saved);
    }

    // Get all users
    @GetMapping
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList()); // Java 8 (no .toList())
    }

    // Get user by id
    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new UserResponse(user);
    }

    // Delete user by id
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }
}
