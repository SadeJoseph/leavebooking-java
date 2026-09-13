package com.example.leavebooking.leavemanagement;

import com.example.leavebooking.leavemanagement.application.LeaveQueryHandler;
import com.example.leavebooking.leavemanagement.application.dto.LeaveAllowanceDTO;
import com.example.leavebooking.leavemanagement.application.dto.LeaveRequestDTO;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

@Component("leaveManagementContextFacade")
@AllArgsConstructor
public class ContextFacade {
  private final LeaveQueryHandler leaveQueryHandler;

  // Return all leave requests
  public Iterable<LeaveRequestDTO> findAllLeaveRequests() {
    return leaveQueryHandler.findAllLeaveRequests();
  }

  // Retrieve all leave allowances and convert each entity into a DTO.
  public Iterable<LeaveAllowanceDTO> findAllLeaveAllowances() {
    return leaveQueryHandler.findAllLeaveAllowances();
  }

  // Retrieve all leave requests belonging to one staff member.
  public Iterable<LeaveRequestDTO> findLeaveRequestsByStaffId(String staffId) {
    return leaveQueryHandler.findLeaveRequestsByStaffId(staffId);
  }

  // the leave allowance belonging to one staff member.
  public LeaveAllowanceDTO findLeaveAllowanceByStaffId(String staffId) {
    return leaveQueryHandler.findLeaveAllowanceByStaffId(staffId);
  }

  // Query pending leave requests for staff assigned to one manager.
  public Iterable<LeaveRequestDTO> findPendingLeaveRequestsByManagerId(
      String managerId) {
    return leaveQueryHandler.findPendingLeaveRequestsByManagerId(managerId);
  }
}
