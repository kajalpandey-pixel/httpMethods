package com.example.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwt.JwtService;
import com.example.user.AppUser;
import com.example.user.InMemoryUserStore;

@RestController
@RequestMapping("/api")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
  private final InMemoryUserStore userStore;
  private final JwtService jwtService;

  public AuthController(InMemoryUserStore userStore, JwtService jwtService) {
    this.userStore = userStore;
    this.jwtService = jwtService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest req) {
    AppUser user = userStore.findByUsername(req.username());
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    boolean ok = userStore.matches(req.password(), user.getPasswordHash());
    if (!ok) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    String token = jwtService.generateToken(user.getUsername(), user.getRoles());
    logger.info("User {} successfully logged in with roles: {}", user.getUsername(), user.getRoles());
    return ResponseEntity.ok(new LoginResponse(token, user.getUsername(), user.getRoles()));
  }
}
