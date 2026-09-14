package com.example.leavebooking.leavemanagement.application.exceptions;

// Raised when a staff member tries to request leavvthat overlaps an existing active leave request.
public class OverlappingLeaveRequestException extends IllegalArgumentException {

  public OverlappingLeaveRequestException() {
    super("Leave request overlaps an existing pending or approved leave request");
  }
}