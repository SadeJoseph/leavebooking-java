package com.example.leavebooking.leavemanagement.application;

import com.example.leavebooking.common.Identity;
import com.example.leavebooking.leavemanagement.application.mappers.LeaveRequestDomainToJpaMapper;
import com.example.leavebooking.leavemanagement.domain.LeaveRequest;
import com.example.leavebooking.leavemanagement.domain.LeaveType;
import com.example.leavebooking.leavemanagement.infrastructure.repositories.LeaveRequestRepository;
import com.example.leavebooking.leavemanagement.ui.commands.AddLeaveRequestCommand;
import com.example.leavebooking.staffmanagement.domain.StaffMember;
import com.example.leavebooking.leavemanagement.application.exceptions.LeaveRequestNotFoundException;
import com.example.leavebooking.leavemanagement.application.mappers.LeaveRequestJpaToDomainMapper;
import com.example.leavebooking.leavemanagement.ui.commands.CancelLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.ui.commands.ApproveLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.ui.commands.RejectLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.application.exceptions.OverlappingLeaveRequestException;
import com.example.leavebooking.leavemanagement.domain.LeaveStatus;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

// Handles command/write operations for leave Management bounded context.
@Service
@AllArgsConstructor
public class LeaveApplicationService {

  private final LeaveRequestRepository leaveRequestRepository;

  // Create and persist a new annual leave request.
  public void addLeaveRequest(AddLeaveRequestCommand command) {
    // Check that the staff member does not already have pending or approved leave
    // covering these dates.
    boolean overlappingRequest = leaveRequestRepository
        .findByStaffId(command.staffId())
        .stream()

        // making sure cancelled and rejected requests should not prevent the dates being requested again.
        .filter(leaveRequest -> leaveRequest.getLeaveStatus() == LeaveStatus.PENDING.ordinal()
            || leaveRequest.getLeaveStatus() == LeaveStatus.APPROVED.ordinal())

        // Two date ranges overlap when: new end is not before existing start AND new start is not after existing end.
        .anyMatch(leaveRequest -> !command.dateRange().endDate()
            .isBefore(leaveRequest.getDateRange().startDate())
            &&
            !command.dateRange().startDate()
                .isAfter(leaveRequest.getDateRange().endDate()));

    if (overlappingRequest) {
      throw new OverlappingLeaveRequestException();
    }
    // Generate the identity for the new LeaveRequest aggregate
    Identity<LeaveRequest> newLeaveRequestId = Identity.generateId();

    // Convert staff id supplied by the command into the identity type used by the domain aggregate.
    Identity<StaffMember> staffId = Identity.of(command.staffId());

    // Construct the domain aggregate first
    LeaveRequest newLeaveRequest = new LeaveRequest(
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
  public void cancelLeaveRequest(CancelLeaveRequestCommand command) {

    LeaveRequest leaveRequest = leaveRequestRepository
        .findById(command.leaveRequestId())
        .map(LeaveRequestJpaToDomainMapper::map)
        .orElseThrow(() -> new LeaveRequestNotFoundException(
            command.leaveRequestId()));

    leaveRequest.cancelLeaveRequest();

    // Map the updated aggregate back to persistence and save it.
    leaveRequestRepository.save(
        LeaveRequestDomainToJpaMapper.map(leaveRequest));
  }

  // Approve an existing leave request.
  public void approveLeaveRequest(ApproveLeaveRequestCommand command) {

    LeaveRequest leaveRequest = leaveRequestRepository
        .findById(command.leaveRequestId())
        .map(LeaveRequestJpaToDomainMapper::map)
        .orElseThrow(() -> new LeaveRequestNotFoundException(
            command.leaveRequestId()));

    // Domain aggregate controls whether approval is valid.
    leaveRequest.approveLeaveRequest();

    leaveRequestRepository.save(
        LeaveRequestDomainToJpaMapper.map(leaveRequest));
  }

  // Reject an existing leave request.
  public void rejectLeaveRequest(RejectLeaveRequestCommand command) {

    LeaveRequest leaveRequest = leaveRequestRepository
        .findById(command.leaveRequestId())
        .map(LeaveRequestJpaToDomainMapper::map)
        .orElseThrow(() -> new LeaveRequestNotFoundException(
            command.leaveRequestId()));

    // controls whether rejection is valid.
    leaveRequest.rejectLeaveRequest();

    leaveRequestRepository.save(
        LeaveRequestDomainToJpaMapper.map(leaveRequest));
  }
}