package com.example.leavebooking.leavemanagement.application.mappers;

import com.example.leavebooking.leavemanagement.domain.DateRange;
import com.example.leavebooking.leavemanagement.domain.LeaveRequest;
import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveRequestJpa;

public class LeaveRequestDomainToJpaMapper {

  public static LeaveRequestJpa map(LeaveRequest leaveRequest) {

    LeaveRequestJpa leaveRequestJpa = new LeaveRequestJpa();

    // Aggregate identity -> persistence id.
    leaveRequestJpa.setId(
        leaveRequest.id().id());

    // LeaveRequest stores the StaffMember identity &the JPA entity stores the
    // String id.
    leaveRequestJpa.setStaffId(
        leaveRequest.staffId().id());

    // Create a new copy of the DateRange value object
    DateRange dateRange = new DateRange(
        leaveRequest.dateRange().startDate(),
        leaveRequest.dateRange().endDate());

    leaveRequestJpa.setDateRange(dateRange);

    leaveRequestJpa.setReason(
        leaveRequest.reason());

    leaveRequestJpa.setLeaveType(
        leaveRequest.leaveType().ordinal());

    leaveRequestJpa.setLeaveStatus(
        leaveRequest.leaveStatus().ordinal());

    leaveRequestJpa.setDescriptionOfStatus(
        leaveRequest.leaveStatus().description());

    return leaveRequestJpa;
  }
}