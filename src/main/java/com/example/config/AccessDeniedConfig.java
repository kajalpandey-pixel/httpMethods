package com.example.config;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class AccessDeniedConfig {

  private static final Logger logger = LoggerFactory.getLogger(AccessDeniedConfig.class);

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return (request, response, exception) -> {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      String username = auth != null ? auth.getName() : "unknown";
      String requestPath = request.getRequestURI();
      String method = request.getMethod();
      
      List<String> roles = auth != null ? 
          auth.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)
              .collect(Collectors.toList()) : 
          List.of();
      
      logger.warn("Forbidden (403) access attempt - User: {}, Roles: {}, Method: {}, Path: {}", 
          username, roles, method, requestPath);
      
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType("application/json");

      String jsonResponse = "{" +
          "\"status\": 403, " +
          "\"error\": \"Forbidden\", " +
          "\"message\": \"You do not have permission to access this resource\", " +
          "\"user\": \"" + username + "\", " +
          "\"roles\": " + roles.toString() + ", " +
          "\"timestamp\": " + System.currentTimeMillis() +
      "}";

      response.getWriter().write(jsonResponse);
    };
  }
}
