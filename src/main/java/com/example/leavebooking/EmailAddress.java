package com.example.leavebooking;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.example.leavebooking.DomainAssertions.argumentNotEmpty;

@EqualsAndHashCode(callSuper = false)
@ToString
public class EmailAddress extends ValueObject {

    public static final String EMAIL_ADDRESS_NOT_EMPTY = "Email address cannot be empty";
    public static final String EMAIL_ADDRESS_NOT_NULL = "Email address to copy cannot be null";

    private final String emailAddress;

    public EmailAddress(String emailAddress) {
        argumentNotEmpty(emailAddress, EMAIL_ADDRESS_NOT_EMPTY);

        this.emailAddress = emailAddress.trim();
    }

    // Shallow copy constructor
    public EmailAddress(EmailAddress emailAddress) {
        if (emailAddress == null) {
            throw new IllegalArgumentException(EMAIL_ADDRESS_NOT_NULL);
        }

        this(emailAddress.emailAddress);
    }

    public String emailAddress() {
        return emailAddress;
    }
}