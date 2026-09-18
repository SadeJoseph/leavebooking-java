package com.example.leavebooking.staffmanagement.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.leavebooking.staffmanagement.ContextFacade;
import com.example.leavebooking.staffmanagement.application.dto.StaffMemberDTO;

@ExtendWith(MockitoExtension.class)
class StaffMemberControllerTests {

  @Mock
  private ContextFacade facade;

  private StaffMemberController staffMemberController;

  @BeforeEach
  void setUp() {

    // SUT - Subject Under Test
    staffMemberController = new StaffMemberController(facade);
  }

  @Test
  @DisplayName("All staff members are returned from the facade")
  void test01() {

    // Arrange
    StaffMemberDTO staffMember = new StaffMemberDTO(
        "0001",
        "Sade",
        "Joseph",
        "sade.joseph@example.com");

    List<StaffMemberDTO> expected = List.of(staffMember);

    when(facade.findAllStaffMembers())
        .thenReturn(expected);

    // Act
    Iterable<StaffMemberDTO> result = staffMemberController.getAllStaffMembers();

    // Assert
    assertSame(expected, result);

    verify(facade)
        .findAllStaffMembers();
  }

  @Test
  @DisplayName("A staff member can be retrieved using their staff id")
  void test02() {

    // Arrange
    StaffMemberDTO expected = new StaffMemberDTO(
        "0001",
        "Sade",
        "Joseph",
        "sade.joseph@example.com");

    when(facade.findStaffMemberById("0001"))
        .thenReturn(expected);

    // Act
    StaffMemberDTO result = staffMemberController
        .getStaffMemberById("0001");

    // Assert
    assertEquals(expected, result);

    verify(facade)
        .findStaffMemberById("0001");
  }
}