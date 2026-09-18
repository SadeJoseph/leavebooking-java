package com.example.leavebooking.staffmanagement;

import com.example.leavebooking.staffmanagement.application.StaffMemberApplicationService;
import com.example.leavebooking.staffmanagement.application.StaffMemberQueryHandler;
import com.example.leavebooking.staffmanagement.application.dto.StaffMemberDTO;

import lombok.AllArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component("staffManagementContextFacade")
@AllArgsConstructor
public class ContextFacade {

  // Query handler used for read-only staff member operations.
  private final StaffMemberQueryHandler staffMemberQueryHandler;

  // Application service used for staff member changes.
  private final StaffMemberApplicationService staffMemberApplicationService;

  // Return all staff member details.
  // The façade delegates the actual query work to the query handler.
  @PreAuthorize("hasRole('ADMIN')")
  public Iterable<StaffMemberDTO> findAllStaffMembers() {

    return staffMemberQueryHandler
        .findAllStaffMembers();
  }

  @PreAuthorize("hasRole('ADMIN')")
  public StaffMemberDTO findStaffMemberById(
      String staffId) {

    return staffMemberQueryHandler
        .findStaffMemberById(staffId);
  }

  // Only an admin can amend a staff member's department.
  @PreAuthorize("hasRole('ADMIN')")
  public void updateDepartment(
      String staffId,
      String department) {

    staffMemberApplicationService
        .updateDepartment(
            staffId,
            department);
  }

  // ADMIN can add a new staff member.
  @PreAuthorize("hasRole('ADMIN')")
  public void addStaffMember(
      String firstName,
      String surname,
      String email,
      String department) {

    staffMemberApplicationService.addStaffMember(
        firstName,
        surname,
        email,
        department);
  }
}