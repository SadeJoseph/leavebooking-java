package com.example.leavebooking.identity.authService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;

import com.example.leavebooking.identity.security.Role;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.example.leavebooking.identity.dto.LoginResponse;

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
      String role) throws Exception {

    // Details to create the user account.
    CreateRequest createRequest = new CreateRequest()
        .setEmail(email)
        .setPassword(password)
        .setDisplayName(username)
        .setEmailVerified(false);

    // Create the account in Firebase Authentication.
    UserRecord userRecord = firebaseAuth.createUser(createRequest);

    // Check that the supplied role is one of our valid Role values.
    String confirmedRole = role != null
        ? Role.fromString(role).getAuthority()
        : Role.USER.name();

    // Add our application-specific data to the Firebase user's JWT claims.
    Map<String, Object> customClaims = Map.of(
        "role", confirmedRole,
        "admin", false);

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