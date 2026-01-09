package com.example.user;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InMemoryUserStore {

  private final Map<String, AppUser> users = new ConcurrentHashMap<>();
  private final PasswordEncoder passwordEncoder;

  public InMemoryUserStore(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;

    initializeDefaultUsers();
  }

  private void initializeDefaultUsers() {
    register("admin", "admin123", List.of("ROLE_ADMIN"));
    register("user", "user123", List.of("ROLE_USER"));
  }

  public AppUser findByUsername(String username) {
    return users.get(username);
  }

  public void register(String username, String rawPassword, List<String> roles) {
    String hash = passwordEncoder.encode(rawPassword);
    users.put(username, new AppUser(username, hash, roles));
  }

  public boolean matches(String rawPassword, String passwordHash) {
    return passwordEncoder.matches(rawPassword, passwordHash);
  }
}
