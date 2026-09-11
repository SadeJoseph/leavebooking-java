package com.example.leavebooking.staffmanagement.ui;

import com.example.leavebooking.staffmanagement.ContextFacade;
import com.example.leavebooking.staffmanagement.application.dto.StaffMemberDTO;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


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
}
