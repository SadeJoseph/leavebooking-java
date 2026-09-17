package com.example.leavebooking.identity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(
    @JsonProperty("localId") String uid,
    String email,
    @JsonProperty("displayName") String username,
    @JsonProperty("idToken") String accessToken,
    String refreshToken,
    @JsonProperty("expiresIn") String expiresInSeconds) {

}