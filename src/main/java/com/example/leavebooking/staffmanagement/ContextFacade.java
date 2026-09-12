package com.example.leavebooking.staffmanagement;

import com.example.leavebooking.staffmanagement.application.StaffMemberQueryHandler;
import com.example.leavebooking.staffmanagement.application.dto.StaffMemberDTO;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

@Component("staffManagementContextFacade")
@AllArgsConstructor
public class ContextFacade {

  // Query handler used for read-only staff member operations.
  private final StaffMemberQueryHandler staffMemberQueryHandler;

  // Return all staff member details.The façade delegates the actual query work to the query handler.
  public Iterable<StaffMemberDTO> findAllStaffMembers() {
    return staffMemberQueryHandler.findAllStaffMembers();
  }

  public StaffMemberDTO findStaffMemberById(String staffId) {
    return staffMemberQueryHandler.findStaffMemberById(staffId);
}
}