package com.example.leavebooking.leavemanagement.application.dto;

import java.time.LocalDate;

// DTO used to return leave request info contains only the data required for viewing a leave request.
public record LeaveRequestDTO(
    String id,
    String staffId,
    LocalDate startDate,
    LocalDate endDate,
    String reason,
    String leaveType,
    String leaveStatus,
    String descriptionOfStatus) {
}