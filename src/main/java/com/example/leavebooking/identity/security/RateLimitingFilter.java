package com.example.leavebooking.identity.security;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RateLimitingFilter extends OncePerRequestFilter {

  // Maximum number of login attempts allowed in one time window.
  private static final int MAX_REQUESTS = 5;

  // Each client gets 5 requests per minute.
  private static final Duration WINDOW = Duration.ofMinutes(1);
  // Stores the current request window for each client. ConcurrentHashMap is used because multiple HTTP requests may be processed by different threads at the same time.
  private final ConcurrentHashMap<String, RequestWindow> requestWindows = new ConcurrentHashMap<>();

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {

    //Only rate limit POST /auth/login.
    return !(request.getRequestURI().equals("/auth/login")
        && request.getMethod().equalsIgnoreCase("POST"));
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    String clientAddress = request.getRemoteAddr();
    Instant now = Instant.now();

    // clientAddressompute() updates the entry atomically for this client, preventing concurrent requests from corrupting the count.

    RequestWindow requestWindow = requestWindows.compute(
        clientAddress,
        (key, currentWindow) -> {

          // No existing window, or the old one has expired.
          if (currentWindow == null
              || now.isAfter(
                  currentWindow.startedAt()
                      .plus(WINDOW))) {

            return new RequestWindow(
                now,
                1);
          }

          // Still within the current minute.
          return new RequestWindow(
              currentWindow.startedAt(),
              currentWindow.requestCount() + 1);
        });

    // Reject requests once the client exceeds the limit.
    if (requestWindow.requestCount() > MAX_REQUESTS) {

      log.warn(
          "Rate limit exceeded for login endpoint by client {}",
          clientAddress);

      response.setStatus(
          HttpStatus.TOO_MANY_REQUESTS.value());

      response.setContentType("application/json");

      response.getWriter().write(
          """
              {
                "error": "Too Many Requests",
                "message": "Too many login attempts. Please try again later."
              }
              """);

      return;
    }

    filterChain.doFilter(request, response);
  }

  // Represents one client's current rate-limiting window.
  private record RequestWindow(
      Instant startedAt,
      int requestCount) {
  }
}