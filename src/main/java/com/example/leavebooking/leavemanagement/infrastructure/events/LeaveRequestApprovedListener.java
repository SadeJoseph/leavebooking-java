package com.example.leavebooking.leavemanagement.infrastructure.events;

import com.example.leavebooking.leavemanagement.application.LeaveAllowanceApplicationService;
import com.example.leavebooking.leavemanagement.domain.events.LeaveRequestApprovedEvent;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AllArgsConstructor
public class LeaveRequestApprovedListener {

    private final LeaveAllowanceApplicationService leaveAllowanceApplicationService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(LeaveRequestApprovedEvent event) {

        log.info(
                "Leave request approved event received for staff ID: {}",
                event.staffId()
        );

        int numberOfDays =
                event.dateRange().numberOfDays();

        leaveAllowanceApplicationService.useLeave(
                event.staffId(),
                numberOfDays
        );
    }
}