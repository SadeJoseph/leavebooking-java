package com.example.leavebooking;

import lombok.ToString;
import static com.example.leavebooking.DomainAssertions.argumentNotEmpty;

@ToString(callSuper = true)
public class LeaveRequest extends Entity<LeaveRequest> implements AggregateRoot{
    
    public static final String STAFF_ID_CANNOT_BE_NULL = "Staff ID cannot be null";
    public static final String DATE_RANGE_CANNOT_BE_NULL = "Date range cannot be null";
    public static final String REASON_CANNOT_BE_EMPTY = "Reason cannot be empty";
    public static final String LEAVE_TYPE_CANNOT_BE_NULL = "Leave type cannot be null";

    private final Identity<StaffMember> staffId;
    private final DateRange dateRange;
    private final String reason;
    private final LeaveType leaveType;

    private LeaveStatus leaveStatus;

    public LeaveRequest(
            Identity<LeaveRequest> id, 
            Identity<StaffMember> staffId,  // keeps aggregate bounday clean 
            DateRange dateRange,
            String reason,
            LeaveType leaveType
    ) {
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
        }
    }

    public void rejectLeaveRequest() {
        if (leaveStatus == LeaveStatus.PENDING) {
            leaveStatus = LeaveStatus.REJECTED;
        }
    }
    public void cancelLeaveRequest() { // leave requests can only be cancelled if they are in PENDING or APPROVED status
        if (leaveStatus == LeaveStatus.PENDING
                || leaveStatus == LeaveStatus.APPROVED) {
            leaveStatus = LeaveStatus.CANCELLED;
        }
    }
}