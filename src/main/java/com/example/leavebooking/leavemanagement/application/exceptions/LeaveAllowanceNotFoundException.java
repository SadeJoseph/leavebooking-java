package com.example.leavebooking.leavemanagement.application.exceptions;

// rased when no leave allowance exists for the supplied staff id.
public class LeaveAllowanceNotFoundException extends RuntimeException {

  public LeaveAllowanceNotFoundException(String staffId) {
    super(staffId);
  }
}