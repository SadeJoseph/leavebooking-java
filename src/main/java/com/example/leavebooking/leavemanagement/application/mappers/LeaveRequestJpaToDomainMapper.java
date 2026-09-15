package com.example.leavebooking.leavemanagement.application.mappers;

import com.example.leavebooking.common.Identity;
import com.example.leavebooking.leavemanagement.domain.DateRange;
import com.example.leavebooking.leavemanagement.domain.LeaveRequest;
import com.example.leavebooking.leavemanagement.domain.LeaveStatus;
import com.example.leavebooking.leavemanagement.domain.LeaveType;
import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveRequestJpa;

import java.util.Objects;

public class LeaveRequestJpaToDomainMapper {

  public static LeaveRequest map(LeaveRequestJpa leaveRequestJpa) {

    Objects.requireNonNull(
        leaveRequestJpa,
        "Leave request JPA entity cannot be null");

    return LeaveRequest.leaveRequestOf(
        Identity.of(leaveRequestJpa.getId()),
        Identity.of(leaveRequestJpa.getStaffId()),
        new DateRange(
            leaveRequestJpa.getDateRange().startDate(),
            leaveRequestJpa.getDateRange().endDate()),
        leaveRequestJpa.getReason(),
        LeaveType.values()[leaveRequestJpa.getLeaveType()],
        LeaveStatus.values()[leaveRequestJpa.getLeaveStatus()]);
  }
}