package com.example.leavebooking.staffmanagement.application.mappers;

import com.example.leavebooking.common.domain.Identity;
import com.example.leavebooking.staffmanagement.domain.StaffMember;
import com.example.leavebooking.staffmanagement.infrastructure.entities.StaffMemberJpa;

public class StaffMemberJpaToDomainMapper {

  public static StaffMember map(
      StaffMemberJpa staffMemberJpa) {

    return new StaffMember(
        Identity.of(staffMemberJpa.getId()),
        staffMemberJpa.getFullName(),
        staffMemberJpa.getEmailAddress(),
        staffMemberJpa.getDepartment());
  }
}