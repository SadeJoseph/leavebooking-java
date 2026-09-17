package com.example.leavebooking.identity.dto;

public record LoginRequest(
    String emailOrUsername,
    String password) {

}
