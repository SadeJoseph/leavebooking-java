package com.example.leavebooking.leavemanagement.application;

import com.example.leavebooking.leavemanagement.application.dto.LeaveAllowanceDTO;
import com.example.leavebooking.leavemanagement.application.dto.LeaveRequestDTO;
import com.example.leavebooking.leavemanagement.application.mappers.LeaveAllowanceJpaToDTOMapper;
import com.example.leavebooking.leavemanagement.application.mappers.LeaveRequestJpaToDTOMapper;
import com.example.leavebooking.leavemanagement.infrastructure.repositories.LeaveAllowanceRepository;
import com.example.leavebooking.leavemanagement.infrastructure.repositories.LeaveRequestRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

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
}