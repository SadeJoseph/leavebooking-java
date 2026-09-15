package com.example.leavebooking.leavemanagement.domain.events;

import com.example.leavebooking.common.events.Event;
import com.example.leavebooking.leavemanagement.domain.DateRange;

import java.time.LocalDate;

// Raised only when an APPROVED leave request is cancelled.The allowance listener will use this to restore the leave days.
public record ApprovedLeaveRequestCancelledEvent(
        LocalDate occurredOn,
        String leaveRequestId,
        String staffId,
        DateRange dateRange
) implements Event {
}