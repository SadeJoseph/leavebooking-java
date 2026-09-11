package com.example.leavebooking.leavemanagement.domain;

public enum LeaveType {
    ANNUAL_LEAVE("Annual leave");

    private final String description;

    LeaveType(String description) {
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        this.description = description;
    }
    public String description() {
        return description;
    }
}