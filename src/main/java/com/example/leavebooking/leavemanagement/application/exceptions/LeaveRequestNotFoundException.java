package com.example.leavebooking.leavemanagement.application.exceptions;

// Raised when a leave request cannot be found for the supplied id
public class LeaveRequestNotFoundException extends RuntimeException {

    public LeaveRequestNotFoundException(String leaveRequestId) {
        super(leaveRequestId);
    }
}