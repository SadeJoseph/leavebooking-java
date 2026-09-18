package com.example.leavebooking.leavemanagement;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.example.leavebooking.leavemanagement.application.LeaveApplicationService;
import com.example.leavebooking.leavemanagement.application.LeaveQueryHandler;
import com.example.leavebooking.leavemanagement.application.dto.LeaveAllowanceDTO;
import com.example.leavebooking.leavemanagement.application.dto.LeaveRequestDTO;
import com.example.leavebooking.leavemanagement.ui.commands.AddLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.ui.commands.ApproveLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.ui.commands.CancelLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.ui.commands.RejectLeaveRequestCommand;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ContextFacade {

  private final LeaveQueryHandler leaveQueryHandler;
  private final LeaveApplicationService leaveApplicationService;

  // ADMIN can view all leave requests across the company.
  @PreAuthorize("hasRole('ADMIN')")
  public Iterable<LeaveRequestDTO> findAllLeaveRequests() {

    return leaveQueryHandler.findAllLeaveRequests();
  }

  // ADMIN can view all leave allowances across the company.
  @PreAuthorize("hasRole('ADMIN')")
  public Iterable<LeaveAllowanceDTO> findAllLeaveAllowances() {

    return leaveQueryHandler.findAllLeaveAllowances();
  }

  // ADMIN can view all outstanding leave requests across the company.
  @PreAuthorize("hasRole('ADMIN')")
  public Iterable<LeaveRequestDTO> findAllPendingLeaveRequests() {

    return leaveQueryHandler
        .findAllPendingLeaveRequests();
  }

  // USER can access only their own leave requests.
  // MANAGER can access their own or staff assigned to them.
  // ADMIN can access any staff member.
  @PreAuthorize("@leaveAuthorisationService.canAccessStaff(#staffId, authentication)")
  public Iterable<LeaveRequestDTO> findLeaveRequestsByStaffId(
      String staffId) {

    return leaveQueryHandler.findLeaveRequestsByStaffId(staffId);
  }

  // Same ownership / manager relationship check for leave allowance.
  @PreAuthorize("@leaveAuthorisationService.canAccessStaff(#staffId, authentication)")
  public LeaveAllowanceDTO findLeaveAllowanceByStaffId(
      String staffId) {

    return leaveQueryHandler.findLeaveAllowanceByStaffId(staffId);
  }

  // MANAGER can only view pending requests for their own team.
  // ADMIN can view any manager's team.
  @PreAuthorize("@leaveAuthorisationService.canAccessManagerTeam(#managerId, authentication)")
  public Iterable<LeaveRequestDTO> findPendingLeaveRequestsByManagerId(
      String managerId) {

    return leaveQueryHandler
        .findPendingLeaveRequestsByManagerId(managerId);
  }

  @PreAuthorize("@leaveAuthorisationService.canAddLeave(#command.staffId, authentication)")
  public void addLeaveRequest(
      AddLeaveRequestCommand command) {

    leaveApplicationService.addLeaveRequest(command);
  }

  @PreAuthorize("@leaveAuthorisationService.canCancelLeave(#command.leaveRequestId, authentication)")
  public void cancelLeaveRequest(
      CancelLeaveRequestCommand command) {

    leaveApplicationService.cancelLeaveRequest(command);
  }

  // ADMIN can approve any request.
  // MANAGER can only approve leave belonging to staff assigned to them.
  @PreAuthorize("@leaveAuthorisationService.canApproveOrReject(#command.leaveRequestId, authentication)")
  public void approveLeaveRequest(
      ApproveLeaveRequestCommand command) {

    leaveApplicationService.approveLeaveRequest(command);
  }

  // ADMIN can reject any request.
  // MANAGER can only reject leave belonging to staff assigned to them.
  @PreAuthorize("@leaveAuthorisationService.canApproveOrReject(#command.leaveRequestId, authentication)")
  public void rejectLeaveRequest(
      RejectLeaveRequestCommand command) {

    leaveApplicationService.rejectLeaveRequest(command);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public void addLeaveAllowance(
      String staffId,
      String firstName,
      String surname,
      String managerId) {

    leaveApplicationService.addLeaveAllowance(
        staffId,
        firstName,
        surname,
        managerId);
  }

  // ADMIN can amend the annual leave entitlement assigned to a staff member.
  @PreAuthorize("hasRole('ADMIN')")
  public void amendYearlyEntitlement(
      String staffId,
      int newEntitlement) {

    leaveApplicationService.amendYearlyEntitlement(
        staffId,
        newEntitlement);
  }
}