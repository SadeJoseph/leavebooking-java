package com.example.leavebooking.hrsimulation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.leavebooking.common.events.DomainEventManager;
import com.example.leavebooking.common.events.NewStaffMemberAddedEvent;

import lombok.AllArgsConstructor;

// simulates external HR system sending events to the application
@Service
@AllArgsConstructor
public class HrSimulationService {

  private final DomainEventManager domainEventManager;

  @Transactional
  public void publishNewStaffMember(
      String staffId,
      String firstName,
      String surname,
      String email,
      String department,
      String managerId) {

    NewStaffMemberAddedEvent event = new NewStaffMemberAddedEvent(
        LocalDate.now(),
        staffId,
        firstName,
        surname,
        email,
        department,
        managerId);

    domainEventManager.manageDomainEvents(
        this.getClass().getSimpleName(),
        List.of(event));
  }
}