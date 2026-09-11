package com.example.leavebooking.common;

import static com.example.leavebooking.common.DomainAssertions.argumentNotEmpty;

import java.time.Instant;
import java.util.UUID;

// Identity is immutable so presented as record 
public record Identity<T>(String id) implements ValueObject {

    public static final String IDENTITY_NOT_EMPTY =
            "Identity value cannot be empty";

    // Runs validation before Java assigns the id field
    public Identity {
        argumentNotEmpty(id, IDENTITY_NOT_EMPTY);
    }

    public String id() {
        return id;
    }
    public static <T> Identity<T> of(String id) {
        return new Identity<>(id);
    }

    public static <T> Identity<T> generateId() {
        String id = UUID.ofEpochMillis(
                Instant.now().toEpochMilli()
        ).toString();

        return new Identity<>(id);
    }
}