package com.example.leavebooking.leavemanagement.domain.events;

import com.example.leavebooking.common.events.Event;
import com.example.leavebooking.leavemanagement.domain.DateRange;

import java.time.LocalDate;

// Domain event raised when a leave request is approved.
// contains the information another aggregate will need to react to the approval.
public record LeaveRequestApprovedEvent(
        LocalDate occurredOn,
        String leaveRequestId,
        String staffId,
        DateRange dateRange
) implements Event {
}