package com.example.leavebooking;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.example.leavebooking.DomainAssertions.argumentLength;
import static com.example.leavebooking.DomainAssertions.argumentNotEmpty;

@EqualsAndHashCode(callSuper = false) // compares objects by the 2 values
@ToString

public class FullName extends ValueObject { //because no identity, we use value object to represent the full name of a person

    public static final int MAX_FIRST_NAME_LENGTH = 20;
    public static final int MAX_SURNAME_LENGTH = 20;

    public static final String FIRST_NAME_NOT_EMPTY = "First name cannot be empty";
    public static final String SURNAME_NOT_EMPTY = "Surname cannot be empty";
    public static final String FULL_NAME_CANNOT_BE_NULL = "Full name to copy cannot be null";

    public static final String FIRST_NAME_LENGTH =
            "First name must be between 1 and ${MAX_FIRST_NAME_LENGTH} characters";

    public static final String SURNAME_LENGTH =
            "Surname must be between 1 and ${MAX_SURNAME_LENGTH} characters";

    private final String surname; // makes state immutable
    private final String firstName;

    public FullName(String firstName, String surname) {
        argumentNotEmpty(firstName, FIRST_NAME_NOT_EMPTY);
        argumentNotEmpty(surname, SURNAME_NOT_EMPTY);

        argumentLength(firstName, 1, MAX_FIRST_NAME_LENGTH, FIRST_NAME_LENGTH);
        argumentLength(surname, 1, MAX_SURNAME_LENGTH, SURNAME_LENGTH);

        this.firstName = firstName.trim();
        this.surname = surname.trim();
    }

    // Shallow copy constructor
    public FullName(FullName fullName) {
        if (fullName == null) {
            throw new IllegalArgumentException(FULL_NAME_CANNOT_BE_NULL);
        }

        this(fullName.firstName, fullName.surname);
    }

    public String firstName() {
        return firstName;
    }

    public String surname() {
        return surname;
    }
}