package com.example.leavebooking.leavemanagement.ui;

import com.example.leavebooking.leavemanagement.ContextFacade;
import com.example.leavebooking.leavemanagement.application.dto.LeaveAllowanceDTO;
import com.example.leavebooking.leavemanagement.application.dto.LeaveRequestDTO;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.leavebooking.leavemanagement.ui.commands.AddLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.ui.commands.CancelLeaveRequestCommand;

@RequestMapping("/leave")
@RestController
@AllArgsConstructor
public class LeaveController {

  private final ContextFacade facade;

  // Return all leave requests.
  @GetMapping("/requests")
  @ResponseStatus(HttpStatus.OK)
  public Iterable<LeaveRequestDTO> getAllLeaveRequests() {
    return facade.findAllLeaveRequests();
  }

  // Return all leave allowances.
  @GetMapping("/allowances")
  @ResponseStatus(HttpStatus.OK)
  public Iterable<LeaveAllowanceDTO> getAllLeaveAllowances() {
    return facade.findAllLeaveAllowances();
  }

  @GetMapping("/requests/staff/{staff_id}")
  @ResponseStatus(HttpStatus.OK)
  public Iterable<LeaveRequestDTO> getLeaveRequestsByStaffId(
      @PathVariable String staff_id) {
    return facade.findLeaveRequestsByStaffId(staff_id);
  }

  @GetMapping("/allowances/staff/{staff_id}")
  @ResponseStatus(HttpStatus.OK)
  public LeaveAllowanceDTO getLeaveAllowanceByStaffId(
      @PathVariable String staff_id) {
    return facade.findLeaveAllowanceByStaffId(staff_id);
  }

  @GetMapping("/requests/manager/{manager_id}/pending")
  @ResponseStatus(HttpStatus.OK)
  public Iterable<LeaveRequestDTO> getPendingLeaveRequestsByManagerId(
      @PathVariable String manager_id) {
    return facade.findPendingLeaveRequestsByManagerId(manager_id);
  }

  // Create a new annual leave request.
  @PostMapping("/requests")
  @ResponseStatus(HttpStatus.CREATED)
  public void addLeaveRequest(
      @RequestBody AddLeaveRequestCommand command) {
    facade.addLeaveRequest(command);
  }

  @PostMapping("/requests/cancel")
  @ResponseStatus(HttpStatus.OK)
  public void cancelLeaveRequest(
      @RequestBody CancelLeaveRequestCommand command) {
    facade.cancelLeaveRequest(command);
  }
}