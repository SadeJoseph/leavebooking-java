package com.example.leavebooking.leavemanagement.application.dto;

// DTO used to return a staff member's annual leave allowance.
public record LeaveAllowanceDTO(
    String id,
    String staffId,
    String firstName,
    String surname,
    String managerId,
    int yearlyEntitlement,
    int remainingBalance,
    int daysUsed) {
}