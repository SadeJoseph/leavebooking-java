package com.example.leavebooking.leavemanagement.application;

import com.example.leavebooking.common.domain.Identity;
import com.example.leavebooking.common.events.DomainEventManager;

import com.example.leavebooking.leavemanagement.application.exceptions.LeaveRequestNotFoundException;
import com.example.leavebooking.leavemanagement.application.exceptions.OverlappingLeaveRequestException;
import com.example.leavebooking.leavemanagement.application.exceptions.LeaveAllowanceNotFoundException;

import com.example.leavebooking.leavemanagement.application.mappers.LeaveAllowanceDomainToJpaMapper;
import com.example.leavebooking.leavemanagement.application.mappers.LeaveAllowanceJpaToDomainMapper;
import com.example.leavebooking.leavemanagement.application.mappers.LeaveRequestDomainToJpaMapper;
import com.example.leavebooking.leavemanagement.application.mappers.LeaveRequestJpaToDomainMapper;

import com.example.leavebooking.leavemanagement.domain.LeaveAllowance;
import com.example.leavebooking.leavemanagement.domain.LeaveRequest;
import com.example.leavebooking.leavemanagement.domain.LeaveStatus;
import com.example.leavebooking.leavemanagement.domain.LeaveType;

import com.example.leavebooking.leavemanagement.infrastructure.repositories.LeaveAllowanceRepository;
import com.example.leavebooking.leavemanagement.infrastructure.repositories.LeaveRequestRepository;

import com.example.leavebooking.leavemanagement.ui.commands.AddLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.ui.commands.ApproveLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.ui.commands.CancelLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.ui.commands.RejectLeaveRequestCommand;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

// Handles command/write operations for Leave Management bounded context.
@Service
@AllArgsConstructor
public class LeaveApplicationService {

  private final LeaveRequestRepository leaveRequestRepository;

  private final LeaveAllowanceRepository leaveAllowanceRepository;

  private final DomainEventManager domainEventManager;

  // Create and persist a new annual leave request.
  public void addLeaveRequest(
      AddLeaveRequestCommand command) {

    // Check that the staff member does not already have
    // pending or approved leave covering these dates.
    boolean overlappingRequest = leaveRequestRepository
        .findByStaffId(command.staffId())
        .stream()

        // Cancelled and rejected requests should
        // not prevent the dates being requested again.
        .filter(leaveRequest -> leaveRequest.getLeaveStatus() == LeaveStatus.PENDING.ordinal()
            ||
            leaveRequest.getLeaveStatus() == LeaveStatus.APPROVED.ordinal())

        // Two date ranges overlap when:
        // new end is not before existing start
        // AND new start is not after existing end.
        .anyMatch(leaveRequest -> !command.dateRange()
            .endDate()
            .isBefore(
                leaveRequest.getDateRange().startDate())
            &&
            !command.dateRange()
                .startDate()
                .isAfter(leaveRequest.getDateRange().endDate()));

    if (overlappingRequest) {
      throw new OverlappingLeaveRequestException();
    }

    // Generate the identity for the new LeaveRequest aggregate.
    Identity<LeaveRequest> newLeaveRequestId = Identity.generateId();

    String staffId = command.staffId();

    // Construct the domain aggregate first.
    LeaveRequest newLeaveRequest = LeaveRequest.leaveRequestOfWithEvent(
        newLeaveRequestId,
        staffId,
        command.dateRange(),
        command.reason(),
        LeaveType.ANNUAL_LEAVE);

    // Convert the valid domain aggregate and save it.
    leaveRequestRepository.save(
        LeaveRequestDomainToJpaMapper.map(
            newLeaveRequest));
  }

  // Cancel an existing leave request.
  public void cancelLeaveRequest(
      CancelLeaveRequestCommand command) {

    LeaveRequest leaveRequest = leaveRequestRepository
        .findById(command.leaveRequestId())
        .map(
            LeaveRequestJpaToDomainMapper::map)
        .orElseThrow(
            () -> new LeaveRequestNotFoundException(
                command.leaveRequestId()));

    leaveRequest.cancelLeaveRequest();

    leaveRequestRepository.save(
        LeaveRequestDomainToJpaMapper.map(
            leaveRequest));

    // Notify subscribers.
    if (leaveRequest.domainEventsExist()) {

      domainEventManager.manageDomainEvents(
          this.getClass().getSimpleName(),
          leaveRequest.listOfDomainEvents());

      leaveRequest.clearDomainEvents();
    }
  }

  // Approve an existing leave request.
  @Transactional
  public void approveLeaveRequest(
      ApproveLeaveRequestCommand command) {

    LeaveRequest leaveRequest = leaveRequestRepository
        .findById(command.leaveRequestId())
        .map(
            LeaveRequestJpaToDomainMapper::map)
        .orElseThrow(
            () -> new LeaveRequestNotFoundException(
                command.leaveRequestId()));

    // Domain aggregate controls whether approval is valid.
    leaveRequest.approveLeaveRequest();

    leaveRequestRepository.save(
        LeaveRequestDomainToJpaMapper.map(
            leaveRequest));

    // Notify subscribers of events raised by the aggregate.
    if (leaveRequest.domainEventsExist()) {

      domainEventManager.manageDomainEvents(
          this.getClass().getSimpleName(),
          leaveRequest.listOfDomainEvents());

      leaveRequest.clearDomainEvents();
    }
  }

  // Reject an existing leave request.
  public void rejectLeaveRequest(
      RejectLeaveRequestCommand command) {

    LeaveRequest leaveRequest = leaveRequestRepository
        .findById(command.leaveRequestId())
        .map(
            LeaveRequestJpaToDomainMapper::map)
        .orElseThrow(
            () -> new LeaveRequestNotFoundException(
                command.leaveRequestId()));

    // Domain aggregate controls whether rejection is valid.
    leaveRequest.rejectLeaveRequest();

    leaveRequestRepository.save(
        LeaveRequestDomainToJpaMapper.map(
            leaveRequest));
  }

  // Amend the annual leave entitlement assigned to a staff member.
  @Transactional
  public void amendYearlyEntitlement(
      String staffId,
      int newEntitlement) {

    LeaveAllowance leaveAllowance = leaveAllowanceRepository
        .findByStaffId(staffId)
        .map(
            LeaveAllowanceJpaToDomainMapper::map)

        .orElseThrow(
            () -> new LeaveAllowanceNotFoundException(
                staffId));

    // Domain aggregate applies the entitlement rules and
    // recalculates the remaining balance.
    leaveAllowance.amendYearlyEntitlement(
        newEntitlement);

    leaveAllowanceRepository.save(
        LeaveAllowanceDomainToJpaMapper.map(
            leaveAllowance));
  }
}