package com.example.leavebooking.identity.authService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.example.leavebooking.identity.dto.LoginResponse;
import com.example.leavebooking.identity.security.Role;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FirebaseAuthService {

  private final FirebaseAuth firebaseAuth;
  private final RestClient restClient;

  @Value("${firebase.web-api-key}")
  private String firebaseApiKey;

  public FirebaseAuthService(FirebaseAuth firebaseAuth) {
    this.firebaseAuth = firebaseAuth;
    this.restClient = RestClient.create();
  }

  public UserRecord registerUser(
      String username,
      String email,
      String password,
      String role,
      String staffId) throws Exception {

    // Confirm that the supplied role is valid and convert it
    String confirmedRole = role != null
        ? Role.fromString(role).getAuthority()
        : Role.USER.getAuthority();

    // USER and MANAGER accounts must be linked
    // to a staff member in app
    if (!confirmedRole.equals(Role.ADMIN.getAuthority())
        && (staffId == null || staffId.isBlank())) {

      throw new IllegalArgumentException(
          "Staff ID is required for user and manager accounts");
    }

    // Details Firebase needs to create the user.
    CreateRequest createRequest = new CreateRequest()
        .setEmail(email)
        .setPassword(password)
        .setDisplayName(username)
        .setEmailVerified(false);

    // Create the user in Firebase Authentication.
    UserRecord userRecord = firebaseAuth.createUser(createRequest);

    Map<String, Object> customClaims = new HashMap<>();

    customClaims.put("role", confirmedRole);

    customClaims.put(
        "admin",
        confirmedRole.equals(
            Role.ADMIN.getAuthority()));

    // Admin does not need staffId for our ownership checks.
    if (staffId != null && !staffId.isBlank()) {
      customClaims.put(
          "staffId",
          staffId.trim());
    }

    firebaseAuth.setCustomUserClaims(
        userRecord.getUid(),
        customClaims);

    return userRecord;
  }

  public LoginResponse loginUser(
      String email,
      String password) {

    if (email == null || password == null) {
      throw new IllegalArgumentException(
          "Email and password must not be null");
    }

    String firebaseLoginUrl = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key="
        + firebaseApiKey;

    Map<String, Object> requestBody = Map.of(
        "email", email,
        "password", password,
        "returnSecureToken", true);

    try {

      return restClient
          .post()
          .uri(firebaseLoginUrl)
          .body(requestBody)
          .retrieve()
          .body(LoginResponse.class);

    } catch (HttpClientErrorException exception) {

      log.error(
          "Firebase Auth error [{}] {}",
          exception.getStatusCode(),
          exception.getResponseBodyAsString());

      throw new IllegalArgumentException(
          "Authentication failed: "
              + exception.getResponseBodyAsString());
    }
  }
}