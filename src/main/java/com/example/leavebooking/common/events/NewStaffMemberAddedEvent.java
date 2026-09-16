package com.example.leavebooking.common.events;

import java.time.LocalDate;

public record NewStaffMemberAddedEvent(
    Long id,
    LocalDate occurredOn,
    String staffId,
    String firstName,
    String surname,
    String email,
    String managerId) implements RemoteEvent {

  // Used when the event is first created before an event-store id exists.
  public NewStaffMemberAddedEvent(
      LocalDate occurredOn,
      String staffId,
      String firstName,
      String surname,
      String email,
      String managerId) {
    this(
        null,
        occurredOn,
        staffId,
        firstName,
        surname,
        email,
        managerId);
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public NewStaffMemberAddedEvent withId(Long newId) {
    return new NewStaffMemberAddedEvent(
        newId,
        this.occurredOn,
        this.staffId,
        this.firstName,
        this.surname,
        this.email,
        this.managerId);
  }
}