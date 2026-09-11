package com.example.leavebooking.staffmanagement.application;

import com.example.leavebooking.staffmanagement.application.dto.StaffMemberDTO;
import com.example.leavebooking.staffmanagement.application.mappers.StaffMemberJpaToDTOMapper;
import com.example.leavebooking.staffmanagement.infrastructure.repositories.StaffMemberRepository;
import com.example.leavebooking.staffmanagement.application.exceptions.StaffMemberNotFoundException;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

// Handles read/query operations for staff members.
@AllArgsConstructor
@Service
public class StaffMemberQueryHandler {

  private StaffMemberRepository staffMemberRepository;

  // Retrieve all staff members and convert each JPA entity to a DTO.
  public Iterable<StaffMemberDTO> findAllStaffMembers() {

    return StreamSupport
        .stream(staffMemberRepository
            .findAll()
            .spliterator(),
            false)
        .map(StaffMemberJpaToDTOMapper::toStaffMemberDTO) // means every JPA entity is converted to a DTO before leaves pplication layer
        .collect(toList());
  }

  public StaffMemberDTO findStaffMemberById(String staffId) {

    return staffMemberRepository
        .findById(staffId)
        .map(StaffMemberJpaToDTOMapper::toStaffMemberDTO)
        .orElseThrow(() -> new StaffMemberNotFoundException(staffId));
  }
}