package com.example.user;

import java.util.List;

public class AppUser {
  private final String username;
  private final String passwordHash; // store ONLY hash
  private final List<String> roles;

  public AppUser(String username, String passwordHash, List<String> roles) {
    this.username = username;
    this.passwordHash = passwordHash;
    this.roles = roles;
  }

  public String getUsername() { return username; }
  public String getPasswordHash() { return passwordHash; }
  public List<String> getRoles() { return roles; }
}
