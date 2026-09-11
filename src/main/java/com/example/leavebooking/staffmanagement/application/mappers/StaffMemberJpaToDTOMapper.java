package com.example.leavebooking.staffmanagement.application.mappers;

import com.example.leavebooking.staffmanagement.application.dto.StaffMemberDTO;
import com.example.leavebooking.staffmanagement.infrastructure.entities.StaffMemberJpa;

import java.util.Objects;

// Data Mapper used to convert persistence objects into DTOs -this prevents the controller/client from working directly with JPA entities.
public class StaffMemberJpaToDTOMapper {

  // Convert a StaffMemberJpa database entity into a StaffMemberDTO.
  public static StaffMemberDTO toStaffMemberDTO(
      StaffMemberJpa staffMember) {
    
    Objects.requireNonNull(
        staffMember,
        "Staff member JPA entity cannot be null");
    // Create the DTO using only the data required by the client.
    return new StaffMemberDTO(
        staffMember.getId(),
        staffMember.getFullName().firstName(),
        staffMember.getFullName().surname(),
        staffMember.getEmailAddress().emailAddress());
  }
}