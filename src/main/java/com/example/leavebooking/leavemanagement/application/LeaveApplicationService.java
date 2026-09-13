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

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

// Handles command/write operations for leave Management bounded context.
@Service
@AllArgsConstructor
public class LeaveApplicationService {

  private final LeaveRequestRepository leaveRequestRepository;

  // Create and persist a new annual leave request.
  public void addLeaveRequest(AddLeaveRequestCommand command) {

    // Generate the identity for the new LeaveRequest aggregate
    Identity<LeaveRequest> newLeaveRequestId = Identity.generateId();

    // Convert staff id supplied by the command into the identity type used by the
    // domain aggregate.
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
}