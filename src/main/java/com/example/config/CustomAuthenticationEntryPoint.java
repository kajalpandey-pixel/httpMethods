package com.example.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {

    String requestPath = request.getRequestURI();
    String method = request.getMethod();

    logger.warn("Unauthorized (401) access attempt on {} {} - Invalid token or missing authentication", method, requestPath);

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");

    String jsonResponse = "{" +
        "\"status\": 401, " +
        "\"error\": \"Unauthorized\", " +
        "\"message\": \"Invalid or missing token. Please provide a valid JWT token in Authorization header.\", " +
        "\"timestamp\": " + System.currentTimeMillis() +
    "}";

    response.getWriter().write(jsonResponse);
  }
}
