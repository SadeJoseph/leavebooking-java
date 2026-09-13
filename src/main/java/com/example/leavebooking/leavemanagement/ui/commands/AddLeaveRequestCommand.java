package com.example.leavebooking.leavemanagement.ui.commands;

import com.example.leavebooking.leavemanagement.domain.DateRange;

// Command containing the information requireto create a new annual leave request.- LR aggregate owns rule so new request starts as pending alwys
public record AddLeaveRequestCommand(
        String staffId,
        DateRange dateRange,
        String reason
) {

}