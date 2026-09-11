package com.example.leavebooking.common;

import static com.example.leavebooking.common.DomainAssertions.argumentLength;
import static com.example.leavebooking.common.DomainAssertions.argumentNotEmpty;

import jakarta.persistence.Embeddable;

@Embeddable

// FullName is immutable data, so it is represented as a record
public record FullName(String firstName,String surname) implements ValueObject {

    public static final int MAX_FIRST_NAME_LENGTH = 20;
    public static final int MAX_SURNAME_LENGTH = 20;

    public static final String FIRST_NAME_NOT_EMPTY = "First name cannot be empty";
    public static final String SURNAME_NOT_EMPTY = "Surname cannot be empty";
    public static final String FULL_NAME_CANNOT_BE_NULL = "Full name to copy cannot be null";
    public static final String FIRST_NAME_LENGTH = "First name must be between 1 and ${MAX_FIRST_NAME_LENGTH} characters";
    public static final String SURNAME_LENGTH = "Surname must be between 1 and ${MAX_SURNAME_LENGTH} characters";

    // Validate and trim values before Java stores them in the record
    public FullName {
        firstName = argumentNotEmpty(
                firstName,
                FIRST_NAME_NOT_EMPTY);

        surname = argumentNotEmpty(
                surname,
                SURNAME_NOT_EMPTY);

        argumentLength(
                firstName,
                1,
                MAX_FIRST_NAME_LENGTH,
                FIRST_NAME_LENGTH);

        argumentLength(
                surname,
                1,
                MAX_SURNAME_LENGTH,
                SURNAME_LENGTH);
    }

    public FullName(FullName fullName) {
        if (fullName == null) {
            throw new IllegalArgumentException(
                    FULL_NAME_CANNOT_BE_NULL);
        }
        this(fullName.firstName,fullName.surname);
    }
}