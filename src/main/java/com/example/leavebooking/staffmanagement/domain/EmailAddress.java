package com.example.leavebooking.staffmanagement.domain;

import jakarta.persistence.Embeddable;

import static com.example.leavebooking.common.DomainAssertions.argumentNotEmpty;

import com.example.leavebooking.common.ValueObject;

// Value object that represents a staff member's email address .Immutable so represented as a record
@Embeddable
public record EmailAddress(String emailAddress) implements ValueObject {

    public static final String EMAIL_ADDRESS_NOT_EMPTY = "Email address cannot be empty";

    public static final String EMAIL_ADDRESS_NOT_NULL = "Email address to copy cannot be null";

    public EmailAddress {
        emailAddress = argumentNotEmpty(
                emailAddress,
                EMAIL_ADDRESS_NOT_EMPTY);
    }

    public EmailAddress(EmailAddress emailAddress) { // Copy constructor

        if (emailAddress == null) {
            throw new IllegalArgumentException(
                    EMAIL_ADDRESS_NOT_NULL);
        }

        this(emailAddress.emailAddress);
    }
}