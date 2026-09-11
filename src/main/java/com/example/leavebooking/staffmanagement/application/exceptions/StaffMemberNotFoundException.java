package com.example.leavebooking.staffmanagement.application.exceptions;

// Thrown when a requested staff member cannot be found.
public class StaffMemberNotFoundException extends RuntimeException {

    public StaffMemberNotFoundException(String staffId) {
        super(staffId);
    }
}