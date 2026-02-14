package com.LucasBot.controller;

import com.LucasBot.dto.UserRegisterRequest;
import com.LucasBot.dto.UserResponse;
import com.LucasBot.entity.User;
import com.LucasBot.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // Helpers (Java 8 safe)
    // =========================
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String requireTrimmed(String value, String fieldName) {
        if (isBlank(value)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " is required");
        }
        return value.trim();
    }

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9\\-\\+\\s]{7,20}$");

    private void validateUsername(String username) {
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "username must be 3-20 chars, only letters/numbers/underscore"
            );
        }
    }

    private void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches() || email.length() > 254) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email format is invalid");
        }
    }

    private void validatePhone(String phone) {
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "phoneNumber format is invalid");
        }
    }

    private void validatePassword(String password) {
        if (password.length() < 8 || password.length() > 72) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password must be 8-72 characters");
        }
        boolean hasLetter = password.chars().anyMatch(Character::isLetter);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(c -> !Character.isLetterOrDigit(c));
        if (!(hasLetter && hasDigit && hasSpecial)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "password must include letters, numbers, and special characters"
            );
        }
    }

    // =========================
    // Endpoints
    // =========================

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody UserRegisterRequest req) {

        String username = requireTrimmed(req.getUsername(), "username");
        String phoneNumber = requireTrimmed(req.getPhoneNumber(), "phoneNumber");
        String email = requireTrimmed(req.getEmail(), "email").toLowerCase(); // normalize

        if (isBlank(req.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password is required");
        }
        String password = req.getPassword();

        // validate
        validateUsername(username);
        validatePhone(phoneNumber);
        validateEmail(email);
        validatePassword(password);

        // uniqueness
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already exists");
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already exists");
        }

        // hash password
        String passwordHash = passwordEncoder.encode(password);

        User user = new User(username, phoneNumber, email, passwordHash);
        User saved = userRepository.save(user);

        return new UserResponse(saved);
    }

    @GetMapping
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new UserResponse(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }
}
