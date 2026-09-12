package com.example.leavebooking.leavemanagement.application.mappers;

import com.example.leavebooking.leavemanagement.application.dto.LeaveRequestDTO;
import com.example.leavebooking.leavemanagement.domain.LeaveStatus;
import com.example.leavebooking.leavemanagement.domain.LeaveType;
import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveRequestJpa;

import java.util.Objects;

public class LeaveRequestJpaToDTOMapper {

  public static LeaveRequestDTO toLeaveRequestDTO(
      LeaveRequestJpa leaveRequest) {

    // guard against mapping a null JPA entity first.
    Objects.requireNonNull(
        leaveRequest,
        "Leave request JPA entity cannot be null");

    // Convert the stored ordinal values back into meaningful enum names.
    LeaveType leaveType = LeaveType.values()[leaveRequest.getLeaveType()];

    LeaveStatus leaveStatus = LeaveStatus.values()[leaveRequest.getLeaveStatus()];

    return new LeaveRequestDTO(
        leaveRequest.getId(),
        leaveRequest.getStaffId(),
        leaveRequest.getDateRange().startDate(),
        leaveRequest.getDateRange().endDate(),
        leaveRequest.getReason(),
        leaveType.name(),
        leaveStatus.name(),
        leaveRequest.getDescriptionOfStatus());
  }
}