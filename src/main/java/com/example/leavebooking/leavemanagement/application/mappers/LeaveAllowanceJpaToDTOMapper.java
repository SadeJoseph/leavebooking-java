package com.example.leavebooking.leavemanagement.application.mappers;

import com.example.leavebooking.leavemanagement.application.dto.LeaveAllowanceDTO;
import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveAllowanceJpa;

import java.util.Objects;

// used to convert LeaveAllowanceJpa into the DTO used by the query/read side.
public class LeaveAllowanceJpaToDTOMapper {

  public static LeaveAllowanceDTO toLeaveAllowanceDTO(
      LeaveAllowanceJpa leaveAllowance) {

    Objects.requireNonNull(
        leaveAllowance,
        "Leave allowance JPA entity cannot be null");

    int daysUsed = leaveAllowance.getYearlyEntitlement()
        - leaveAllowance.getRemainingBalance();

    return new LeaveAllowanceDTO(
        leaveAllowance.getId(),
        leaveAllowance.getStaffId(),
        leaveAllowance.getStaffName().firstName(),
        leaveAllowance.getStaffName().surname(),
        leaveAllowance.getManagerId(),
        leaveAllowance.getYearlyEntitlement(),
        leaveAllowance.getRemainingBalance(),
        daysUsed);
  }
}