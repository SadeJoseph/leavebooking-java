package com.example.leavebooking.leavemanagement.application;

import com.example.leavebooking.leavemanagement.application.dto.LeaveAllowanceDTO;
import com.example.leavebooking.leavemanagement.application.dto.LeaveRequestDTO;
import com.example.leavebooking.leavemanagement.application.mappers.LeaveAllowanceJpaToDTOMapper;
import com.example.leavebooking.leavemanagement.application.mappers.LeaveRequestJpaToDTOMapper;
import com.example.leavebooking.leavemanagement.infrastructure.repositories.LeaveAllowanceRepository;
import com.example.leavebooking.leavemanagement.infrastructure.repositories.LeaveRequestRepository;
import com.example.leavebooking.leavemanagement.application.exceptions.LeaveAllowanceNotFoundException;
import com.example.leavebooking.leavemanagement.domain.LeaveStatus;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.stream.StreamSupport;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@AllArgsConstructor
@Service
public class LeaveQueryHandler {
  // Repo used to retrieve leave requests.
  private final LeaveRequestRepository leaveRequestRepository;

  // Repo used to retrieve leave allowances.
  private final LeaveAllowanceRepository leaveAllowanceRepository;

  public Iterable<LeaveRequestDTO> findAllLeaveRequests() {

    return StreamSupport
        .stream(leaveRequestRepository
            .findAll()
            .spliterator(),
            false)
        .map(LeaveRequestJpaToDTOMapper::toLeaveRequestDTO)
        .collect(toList());
  }

  // Retrieve all leave allowances and convert each entity into a DTO.
  public Iterable<LeaveAllowanceDTO> findAllLeaveAllowances() {
    return StreamSupport
        .stream(
            leaveAllowanceRepository
                .findAll()
                .spliterator(),
            false)
        .map(LeaveAllowanceJpaToDTOMapper::toLeaveAllowanceDTO)
        .collect(toList());
  }

  // Retrieve all leave requests belonging to one staff member.
  public Iterable<LeaveRequestDTO> findLeaveRequestsByStaffId(String staffId) {

    return leaveRequestRepository
        .findByStaffId(staffId)
        .stream()
        .map(LeaveRequestJpaToDTOMapper::toLeaveRequestDTO)
        .collect(toList());
  }

  // Retrieve the leave allowance belonging to one staff member.
  public LeaveAllowanceDTO findLeaveAllowanceByStaffId(String staffId) {

    return leaveAllowanceRepository
        .findByStaffId(staffId)
        .map(LeaveAllowanceJpaToDTOMapper::toLeaveAllowanceDTO)
        .orElseThrow(() -> new LeaveAllowanceNotFoundException(staffId));
  }

  // Retrieve pending leave requests for staff assigned to one manager.
  public Iterable<LeaveRequestDTO> findPendingLeaveRequestsByManagerId(
      String managerId) {

    // find all staff members assigned to this manager.
    Set<String> managedStaffIds = leaveAllowanceRepository
        .findByManagerId(managerId)
        .stream()
        .map(leaveAllowance -> leaveAllowance.getStaffId())
        .collect(toSet());

    // Then retrieve pending leave requests and keep only those belongin to staff managed by this manager.
    return leaveRequestRepository
        .findByLeaveStatus(LeaveStatus.PENDING.ordinal())
        .stream()
        .filter(leaveRequest -> managedStaffIds.contains(
            leaveRequest.getStaffId()))
        .map(LeaveRequestJpaToDTOMapper::toLeaveRequestDTO)
        .collect(toList());
  }
}