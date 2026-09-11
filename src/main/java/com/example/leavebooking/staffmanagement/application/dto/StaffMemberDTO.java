package com.example.leavebooking.staffmanagement.application.dto;

// DTO used to transfer staff member data between the application layer and the client without exposing the domain or JPA entity directly.
public record StaffMemberDTO(String id,String firstName,String surname, String emailAddress) {

    // Error messages used when validating incoming/outgoing DTO data.
    public static final String ID_BLANK = "Staff member ID is required";
    public static final String FIRST_NAME_BLANK = "A first name is required";
    public static final String SURNAME_BLANK = "A surname is required";
    public static final String EMAIL_ADDRESS_BLANK = "An email address is required";
    public static final String FIRST_NAME_TOO_LONG = "First name must be under 40 characters";
    public static final String SURNAME_TOO_LONG = "Surname must be under 40 characters";
    public static final int FIRST_NAME_MAX_LENGTH = 40;
    public static final int SURNAME_MAX_LENGTH = 40;

    // records because they are immutable data objects.
    public StaffMemberDTO {

        // Remove whitespace before validating.
        if (id != null) id = id.trim();
        if (firstName != null) firstName = firstName.trim();
        if (surname != null) surname = surname.trim();
        if (emailAddress != null) emailAddress = emailAddress.trim();

        // ID must be provided.
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(ID_BLANK);
        }
        // First name must be provided.
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException(FIRST_NAME_BLANK);
        }
        // First name cannot exceed the permitted DTO length.
        if (firstName.length() > FIRST_NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(FIRST_NAME_TOO_LONG);
        }
        // Surname must be provided.
        if (surname == null || surname.isBlank()) {
            throw new IllegalArgumentException(SURNAME_BLANK);
        }
        // Surname cannot exceed the permitted DTO length.
        if (surname.length() > SURNAME_MAX_LENGTH) {
            throw new IllegalArgumentException(SURNAME_TOO_LONG);
        }
        // Email address must be provided.
        if (emailAddress == null || emailAddress.isBlank()) {
            throw new IllegalArgumentException(EMAIL_ADDRESS_BLANK);
        }
    }
}