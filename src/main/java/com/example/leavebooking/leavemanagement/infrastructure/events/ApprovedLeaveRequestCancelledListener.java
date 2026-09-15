package com.example.leavebooking.leavemanagement.infrastructure.events;

import com.example.leavebooking.leavemanagement.application.LeaveAllowanceApplicationService;
import com.example.leavebooking.leavemanagement.domain.events.ApprovedLeaveRequestCancelledEvent;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AllArgsConstructor
public class ApprovedLeaveRequestCancelledListener {

  private final LeaveAllowanceApplicationService leaveAllowanceApplicationService;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(ApprovedLeaveRequestCancelledEvent event) {

    log.info(
        "Approved leave request cancelled event received for staff ID: {}",
        event.staffId());

    int numberOfDays = event.dateRange().numberOfDays();

    leaveAllowanceApplicationService.restoreLeave(
        event.staffId(),
        numberOfDays);
  }
}