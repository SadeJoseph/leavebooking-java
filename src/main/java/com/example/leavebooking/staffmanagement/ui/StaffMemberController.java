package com.example.leavebooking.staffmanagement.ui;

import com.example.leavebooking.staffmanagement.ContextFacade;
import com.example.leavebooking.staffmanagement.application.dto.StaffMemberDTO;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RequestMapping("/staff")
@RestController
@AllArgsConstructor
public class StaffMemberController {

  private final ContextFacade facade;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Iterable<StaffMemberDTO> getAllStaffMembers() {

    return facade.findAllStaffMembers();
  }

  @GetMapping("/{staff_id}")
  @ResponseStatus(HttpStatus.OK)
  public StaffMemberDTO getStaffMemberById(
      @PathVariable String staff_id) {

    return facade.findStaffMemberById(staff_id);
  }

  @PatchMapping("/{staff_id}/department")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateDepartment(
      @PathVariable String staff_id,
      @RequestBody UpdateDepartmentRequest request) {

    facade.updateDepartment(
        staff_id,
        request.department());
  }

  public record UpdateDepartmentRequest(
      String department) {
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void addStaffMember(
      @RequestBody AddStaffMemberRequest request) {

    facade.addStaffMember(
        request.firstName(),
        request.surname(),
        request.email(),
        request.department(),
        request.managerId());
  }

  public record AddStaffMemberRequest(
      String firstName,
      String surname,
      String email,
      String department,
      String managerId) {
  }
}