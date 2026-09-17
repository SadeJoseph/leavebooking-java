package com.example.leavebooking.identity.dto;

public record RegisterResponse(
    String uid,
    String email,
    String username,
    String message) {
}