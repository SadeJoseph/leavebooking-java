package com.example.leavebooking;

public enum LeaveStatus {

    PENDING("Awaiting approval"),
    APPROVED("Leave request approved"),
    REJECTED("Leave request rejected"),
    CANCELLED("Leave request cancelled");

    private final String description;

    LeaveStatus(String description) {
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        this.description = description;
    }
    public String description() {
        return description;
    }
}