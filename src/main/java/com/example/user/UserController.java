package com.example.user;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);
  private final InMemoryUserStore userStore;

  public UserController(InMemoryUserStore userStore) {
    this.userStore = userStore;
  }

  @PreAuthorize("hasAnyRole('BASIC', 'ADMIN')")
  @GetMapping
  public ResponseEntity<?> getAllUsers() {
    List<UserDto> users = userStore.getAllUsers().stream()
        .map(u -> new UserDto(u.getUsername(), u.getRoles()))
        .collect(Collectors.toList());
    logger.info("User requested to view all users. Total users: {}", users.size());
    return ResponseEntity.ok(users);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<?> addUser(@RequestBody AddUserRequest req) {
    if (req.username() == null || req.password() == null) {
      return ResponseEntity.badRequest().body("Username and password are required");
    }

    AppUser existing = userStore.findByUsername(req.username());
    if (existing != null) {
      logger.warn("Attempt to add duplicate user: {}", req.username());
      return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
    }

    userStore.register(req.username(), req.password(), List.of("ROLE_BASIC"));
    logger.info("New user {} registered with ROLE_BASIC by admin", req.username());
    return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
  }

  public record UserDto(String username, List<String> roles) {}

  public record AddUserRequest(String username, String password) {}
}
