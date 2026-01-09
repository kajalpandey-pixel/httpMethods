package com.example.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.user.AppUser;
import com.example.user.InMemoryUserStore;
import com.example.jwt.JwtService;

@RestController
@RequestMapping("/api")
public class AuthController {

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
    return ResponseEntity.ok(new LoginResponse(token, user.getUsername(), user.getRoles()));
  }
}
