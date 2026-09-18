package com.example.leavebooking.staffmanagement.application.mappers;

import com.example.leavebooking.staffmanagement.domain.StaffMember;
import com.example.leavebooking.staffmanagement.infrastructure.entities.StaffMemberJpa;

public class StaffMemberDomainToJpaMapper {

  public static StaffMemberJpa map(StaffMember staffMember) {

    StaffMemberJpa staffMemberJpa = new StaffMemberJpa();

    staffMemberJpa.setId(
        staffMember.id().id());

    staffMemberJpa.setFullName(
        staffMember.fullName());

    staffMemberJpa.setEmailAddress(
        staffMember.emailAddress());

    staffMemberJpa.setDepartment(
        staffMember.department());

    return staffMemberJpa;
  }
}