package com.example.leavebooking.identity.security;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class RateLimitingFilterTests {

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private FilterChain filterChain;

  private RateLimitingFilter rateLimitingFilter;

  @BeforeEach
  void setUp() {

    MockitoAnnotations.openMocks(this);

    // SUT - Subject Under Test
    rateLimitingFilter = new RateLimitingFilter();
  }

  @Test
  @DisplayName("A sixth login request within the rate limit window is rejected")
  void test01() throws Exception {

    // Arrange
    when(request.getRequestURI())
        .thenReturn("/auth/login");

    when(request.getMethod())
        .thenReturn("POST");

    when(request.getRemoteAddr())
        .thenReturn("127.0.0.1");

    StringWriter responseBody = new StringWriter();

    when(response.getWriter())
        .thenReturn(
            new PrintWriter(responseBody));

    // Act
    for (int requestNumber = 1; requestNumber <= 6; requestNumber++) {

      rateLimitingFilter.doFilter(
          request,
          response,
          filterChain);
    }

    // Assert
    verify(filterChain, times(5))
        .doFilter(request, response);

    verify(response)
        .setStatus(429);
  }

  @Test
  @DisplayName("Requests to other endpoints are not rate limited")
  void test02() throws Exception {

    // Arrange
    when(request.getRequestURI())
        .thenReturn("/leave/requests");

    when(request.getMethod())
        .thenReturn("GET");

    // Act
    rateLimitingFilter.doFilter(
        request,
        response,
        filterChain);

    // Assert
    verify(filterChain)
        .doFilter(request, response);

    verify(response, never())
        .setStatus(429);
  }
}