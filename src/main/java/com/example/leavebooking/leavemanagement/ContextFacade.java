package com.example.leavebooking.leavemanagement;

import com.example.leavebooking.leavemanagement.application.LeaveApplicationService;
import com.example.leavebooking.leavemanagement.application.LeaveQueryHandler;
import com.example.leavebooking.leavemanagement.application.dto.LeaveAllowanceDTO;
import com.example.leavebooking.leavemanagement.application.dto.LeaveRequestDTO;
import com.example.leavebooking.leavemanagement.ui.commands.AddLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.ui.commands.CancelLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.ui.commands.ApproveLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.ui.commands.RejectLeaveRequestCommand;

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

  private final LeaveApplicationService leaveApplicationService;

  // Command to create a new annual leave request.
  public void addLeaveRequest(AddLeaveRequestCommand command) {
    leaveApplicationService.addLeaveRequest(command);
  }

  // Command to cancel an existing leave request.
  public void cancelLeaveRequest(CancelLeaveRequestCommand command) {
    leaveApplicationService.cancelLeaveRequest(command);
  }
// approve
  public void approveLeaveRequest(ApproveLeaveRequestCommand command) {
    leaveApplicationService.approveLeaveRequest(command);
  }
// reject
  public void rejectLeaveRequest(RejectLeaveRequestCommand command) {
    leaveApplicationService.rejectLeaveRequest(command);
  }
}
