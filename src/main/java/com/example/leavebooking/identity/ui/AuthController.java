package com.example.leavebooking.identity.ui;

import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.leavebooking.identity.authService.FirebaseAuthService;
import com.example.leavebooking.identity.dto.RegisterRequest;
import com.example.leavebooking.identity.dto.RegisterResponse;
import com.google.firebase.auth.UserRecord;
import com.example.leavebooking.identity.dto.LoginRequest;
import com.example.leavebooking.identity.dto.LoginResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

  public final String USER_CREATED_CONFIRMATION = "User created successfully";

  private final FirebaseAuthService firebaseAuthService;

  @PostMapping("/register")
  public ResponseEntity<?> register(
      @RequestBody RegisterRequest request) throws Exception {

    log.info("Registering user {}", request);

    UserRecord userRecord = firebaseAuthService.registerUser(
        request.username(),
        request.email(),
        request.password(),
        request.role());

    RegisterResponse response = new RegisterResponse(
        userRecord.getUid(),
        userRecord.getEmail(),
        userRecord.getDisplayName(),
        USER_CREATED_CONFIRMATION);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(
      @RequestBody LoginRequest request) {

    LoginResponse response = firebaseAuthService.loginUser(
        request.emailOrUsername(),
        request.password());

    return ResponseEntity.ok(response);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/role-check")
  public ResponseEntity<String> roleCheck(
      Authentication authentication) {

    String roles = authentication
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(", "));

    return ResponseEntity.ok(
        roles + " access granted");
  }
}
