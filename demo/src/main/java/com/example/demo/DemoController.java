package com.example.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

  @GetMapping("/")
  public String welcome() {
    return "{\"message\":\"JWT Authentication API - POST to /api/login with {username, password} credentials to get token\"}";  
  }

  @GetMapping("/api/hello")
  public String hello() {
    return "Hello, authenticated user!";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/api/admin")
  public String adminOnly() {
    return "Hello, admin!";
  }
}
