package com.example.leavebooking;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

import static com.example.leavebooking.DomainAssertions.argumentNotEmpty;

@ToString 
@EqualsAndHashCode(callSuper = false)
public final class Identity<T> extends ValueObject {
    public static final String IDENTITY_NOT_EMPTY = "Identity value cannot be empty";

    private final String id;

    private Identity(String id) {
        argumentNotEmpty(id, IDENTITY_NOT_EMPTY);
        this.id = id;
    }
    public String id() {
        return id;
    }
    public static <T> Identity<T> of(String id) {
        return new Identity<>(id);
    }

    public static <T> Identity<T> generateId() {
        String id = UUID.ofEpochMillis(Instant.now().toEpochMilli()).toString();
        return new Identity<>(id);
    }
}
