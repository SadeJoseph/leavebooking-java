package com.example.leavebooking.leavemanagement.ui;

import com.example.leavebooking.leavemanagement.ContextFacade;
import com.example.leavebooking.leavemanagement.application.dto.LeaveAllowanceDTO;
import com.example.leavebooking.leavemanagement.application.dto.LeaveRequestDTO;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
}