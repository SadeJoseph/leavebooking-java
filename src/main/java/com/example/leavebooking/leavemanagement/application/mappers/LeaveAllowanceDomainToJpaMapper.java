package com.example.leavebooking.leavemanagement.application.mappers;

import com.example.leavebooking.leavemanagement.domain.LeaveAllowance;
import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveAllowanceJpa;

import java.util.Objects;

// Maps the LeaveAllowance domain aggregate
public class LeaveAllowanceDomainToJpaMapper {

  public static LeaveAllowanceJpa map(
      LeaveAllowance leaveAllowance) {

    Objects.requireNonNull(
        leaveAllowance,
        "Leave allowance cannot be null");

    LeaveAllowanceJpa leaveAllowanceJpa = new LeaveAllowanceJpa();

    leaveAllowanceJpa.setId(
        leaveAllowance.id().id());

    leaveAllowanceJpa.setStaffId(
        leaveAllowance.staffId().id());

    leaveAllowanceJpa.setStaffName(
        leaveAllowance.staffName());

    leaveAllowanceJpa.setManagerId(
        leaveAllowance.managerId().id());

    leaveAllowanceJpa.setYearlyEntitlement(
        leaveAllowance.yearlyEntitlement());

    leaveAllowanceJpa.setRemainingBalance(
        leaveAllowance.remainingBalance());

    return leaveAllowanceJpa;
  }
}