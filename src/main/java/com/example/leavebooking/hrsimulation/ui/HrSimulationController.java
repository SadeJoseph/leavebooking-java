package com.example.leavebooking.hrsimulation.ui;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.leavebooking.hrsimulation.HrSimulationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/hr-simulation")
@AllArgsConstructor
public class HrSimulationController {

  private final HrSimulationService hrSimulationService;

  @PostMapping("/staff")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void addStaffMember(
      @RequestBody NewStaffMemberRequest request) {

    hrSimulationService.publishNewStaffMember(
        request.staffId(),
        request.firstName(),
        request.surname(),
        request.email(),
        request.department(),
        request.managerId());
  }

  // Request body used only to simulate a message arriving from the external HR system.
  public record NewStaffMemberRequest(
      String staffId,
      String firstName,
      String surname,
      String email,
      String department,
      String managerId) {
  }
}