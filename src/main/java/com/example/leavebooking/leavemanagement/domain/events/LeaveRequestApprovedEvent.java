package com.example.leavebooking.leavemanagement.domain.events;

import java.time.LocalDate;

import com.example.leavebooking.common.events.LocalEvent;
import com.example.leavebooking.leavemanagement.domain.DateRange;

public record LeaveRequestApprovedEvent(
    Long id,
    LocalDate occurredOn,
    String leaveRequestId,
    String staffId,
    DateRange dateRange) implements LocalEvent {

  public LeaveRequestApprovedEvent(
      LocalDate occurredOn,
      String leaveRequestId,
      String staffId,
      DateRange dateRange) {
    this(
        null,
        occurredOn,
        leaveRequestId,
        staffId,
        dateRange);
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public LeaveRequestApprovedEvent withId(Long newId) {
    return new LeaveRequestApprovedEvent(
        newId,
        this.occurredOn,
        this.leaveRequestId,
        this.staffId,
        this.dateRange);
  }
}