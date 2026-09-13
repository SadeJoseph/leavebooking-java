package com.example.leavebooking.leavemanagement.application.mappers;

import com.example.leavebooking.common.Identity;
import com.example.leavebooking.leavemanagement.domain.DateRange;
import com.example.leavebooking.leavemanagement.domain.LeaveRequest;
import com.example.leavebooking.leavemanagement.domain.LeaveStatus;
import com.example.leavebooking.leavemanagement.domain.LeaveType;
import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveRequestJpa;
import com.example.leavebooking.staffmanagement.domain.StaffMember;

import java.util.Objects;

public class LeaveRequestJpaToDomainMapper {

  public static LeaveRequest map(LeaveRequestJpa leaveRequestJpa) {

    Objects.requireNonNull(
        leaveRequestJpa,
        "Leave request JPA entity cannot be null");

    Identity<LeaveRequest> leaveRequestId = Identity.of(leaveRequestJpa.getId());

    Identity<StaffMember> staffId = Identity.of(leaveRequestJpa.getStaffId());

    DateRange dateRange = new DateRange(
        leaveRequestJpa.getDateRange().startDate(),
        leaveRequestJpa.getDateRange().endDate());


    LeaveRequest leaveRequest = new LeaveRequest(
        leaveRequestId,
        staffId,
        dateRange,
        leaveRequestJpa.getReason(),
        LeaveType.values()[leaveRequestJpa.getLeaveType()]);


    LeaveStatus leaveStatus = LeaveStatus.values()[leaveRequestJpa.getLeaveStatus()];

    if (leaveStatus == LeaveStatus.APPROVED) {
      leaveRequest.approveLeaveRequest();
    } else if (leaveStatus == LeaveStatus.REJECTED) {
      leaveRequest.rejectLeaveRequest();
    } else if (leaveStatus == LeaveStatus.CANCELLED) {
      leaveRequest.cancelLeaveRequest();
    }

    return leaveRequest;
  }
}