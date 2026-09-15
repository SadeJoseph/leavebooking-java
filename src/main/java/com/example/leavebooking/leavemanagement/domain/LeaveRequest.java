package com.example.leavebooking.leavemanagement.domain;

import lombok.ToString;

import static com.example.leavebooking.common.DomainAssertions.argumentNotEmpty;

import com.example.leavebooking.common.AggregateRoot;
import com.example.leavebooking.common.Entity;
import com.example.leavebooking.common.Identity;
import com.example.leavebooking.staffmanagement.domain.StaffMember;
import com.example.leavebooking.leavemanagement.domain.events.LeaveRequestApprovedEvent;
import com.example.leavebooking.leavemanagement.domain.events.ApprovedLeaveRequestCancelledEvent;

import java.time.LocalDate;

@ToString(callSuper = true)
public class LeaveRequest extends AggregateRoot<LeaveRequest> {

  public static final String STAFF_ID_CANNOT_BE_NULL = "Staff ID cannot be null";
  public static final String DATE_RANGE_CANNOT_BE_NULL = "Date range cannot be null";
  public static final String REASON_CANNOT_BE_EMPTY = "Reason cannot be empty";
  public static final String LEAVE_TYPE_CANNOT_BE_NULL = "Leave type cannot be null";

  private final Identity<StaffMember> staffId;
  private final DateRange dateRange;
  private final String reason;
  private final LeaveType leaveType;

  private LeaveStatus leaveStatus;

  private LeaveRequest(
      Identity<LeaveRequest> id,
      Identity<StaffMember> staffId, // keeps aggregate bounday clean
      DateRange dateRange,
      String reason,
      LeaveType leaveType) {
    super(id);

    if (staffId == null) {
      throw new IllegalArgumentException(STAFF_ID_CANNOT_BE_NULL);
    }
    if (dateRange == null) {
      throw new IllegalArgumentException(DATE_RANGE_CANNOT_BE_NULL);
    }
    argumentNotEmpty(reason, REASON_CANNOT_BE_EMPTY);
    if (leaveType == null) {
      throw new IllegalArgumentException(LEAVE_TYPE_CANNOT_BE_NULL);
    }

    this.staffId = staffId;
    this.dateRange = new DateRange(dateRange);
    this.reason = reason.trim();
    this.leaveType = leaveType;
    this.leaveStatus = LeaveStatus.PENDING; // requests enter the system with a default status of PENDING
  }

  public Identity<LeaveRequest> id() {
    return id;
  }

  public Identity<StaffMember> staffId() {
    return staffId;
  }

  public DateRange dateRange() {
    return dateRange;
  }

  public String reason() {
    return reason;
  }

  public LeaveType leaveType() {
    return leaveType;
  }

  public LeaveStatus leaveStatus() {
    return leaveStatus;
  }

  public void approveLeaveRequest() {
    if (leaveStatus == LeaveStatus.PENDING) {
      leaveStatus = LeaveStatus.APPROVED;
      addDomainEvent(
          new LeaveRequestApprovedEvent(
              LocalDate.now(),
              id().id(),
              staffId.id(),
              dateRange));
    }
  }

  public void rejectLeaveRequest() {
    if (leaveStatus == LeaveStatus.PENDING) {
      leaveStatus = LeaveStatus.REJECTED;
    }
  }

  public void cancelLeaveRequest() { // leave requests can only be cancelled if they are in PENDING or APPROVE status
    if (leaveStatus == LeaveStatus.PENDING
        || leaveStatus == LeaveStatus.APPROVED) {
      // cancellation is only allowed for PENDING or APPROVED requests.
    boolean wasApproved = leaveStatus == LeaveStatus.APPROVED;
    leaveStatus = LeaveStatus.CANCELLED;
    // Only approved leave has previously reduced the allowance mso only approv leave needs a cancellation event.
    if (wasApproved) {

      addDomainEvent(
          new ApprovedLeaveRequestCancelledEvent(
            LocalDate.now(),
            id().id(),
            staffId.id(),
            dateRange));
    }

  } 
 
    }
    

  // Used when reconstructing an existing LeaveRequest
  public static LeaveRequest leaveRequestOf(
      Identity<LeaveRequest> id,
      Identity<StaffMember> staffId,
      DateRange dateRange,
      String reason,
      LeaveType leaveType,
      LeaveStatus leaveStatus) {
    LeaveRequest leaveRequest = new LeaveRequest(
        id,
        staffId,
        dateRange,
        reason,
        leaveType);

    leaveRequest.leaveStatus = leaveStatus;

    return leaveRequest;
  }

  // Used when creating a brand new LeaveRequest.
  public static LeaveRequest leaveRequestOfWithEvent(
      Identity<LeaveRequest> id,
      Identity<StaffMember> staffId,
      DateRange dateRange,
      String reason,
      LeaveType leaveType) {
    LeaveRequest leaveRequest = new LeaveRequest(
        id,
        staffId,
        dateRange,
        reason,
        leaveType);

    return leaveRequest;
  }

}