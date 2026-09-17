package com.example.leavebooking.identity.authService;

import java.io.IOException;
import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FirebaseTokenFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    final String AUTH_HEADER = request.getHeader("Authorization");

    final String BEARER = "Bearer ";

    if (AUTH_HEADER != null
        && AUTH_HEADER.startsWith(BEARER)) {
      final String token = AUTH_HEADER.substring(BEARER.length());

      try {

        UsernamePasswordAuthenticationToken authentication = getUsernamePasswordAuthenticationToken(token);

        SecurityContextHolder
            .getContext()
            .setAuthentication(authentication);

      } catch (FirebaseAuthException exception) {

        // Invalid, expired or tampered token.
        SecurityContextHolder.clearContext();

        response.setStatus(
            HttpServletResponse.SC_UNAUTHORIZED);

        return;
      }
    }

    filterChain.doFilter(request, response);
  }

  private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(
      String token) throws FirebaseAuthException {

    FirebaseToken decodedToken = FirebaseAuth
        .getInstance()
        .verifyIdToken(token);

    final String role = (String) decodedToken
        .getClaims()
        .get("role");

    log.info(
        "role retrieved from token {}",
        role);

    List<GrantedAuthority> authorities = List.of(
        new SimpleGrantedAuthority(role));

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        decodedToken.getUid(),
        null,
        authorities);

    authentication.setDetails(decodedToken);

    return authentication;
  }
}