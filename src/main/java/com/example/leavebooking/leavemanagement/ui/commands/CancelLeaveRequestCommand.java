package com.example.leavebooking.leavemanagement.ui.commands;

// contasins info required to cancel an existing leave request.
public record CancelLeaveRequestCommand(
        String leaveRequestId
) {
}