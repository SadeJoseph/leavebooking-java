package com.example.leavebooking.leavemanagement.ui;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.leavebooking.leavemanagement.ContextFacade;
import com.example.leavebooking.leavemanagement.application.dto.LeaveRequestDTO;
import com.example.leavebooking.leavemanagement.domain.DateRange;
import com.example.leavebooking.leavemanagement.ui.commands.AddLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.ui.commands.ApproveLeaveRequestCommand;

@ExtendWith(MockitoExtension.class)
class LeaveControllerTests {

  @Mock
  private ContextFacade facade;

  private LeaveController leaveController;

  @BeforeEach
  void setUp() {
    leaveController = new LeaveController(facade);
  }

  @Test
  @DisplayName("All leave requests are returned from the facade")
  void test01() {

    // Arrange
    List<LeaveRequestDTO> expected = List.of();

    when(facade.findAllLeaveRequests())
        .thenReturn(expected);

    // Act
    Iterable<LeaveRequestDTO> result = leaveController.getAllLeaveRequests();

    // Assert
    assertSame(expected, result);

    verify(facade)
        .findAllLeaveRequests();
  }

  @Test
  @DisplayName("Leave requests can be retrieved using a staff id")
  void test02() {

    // Arrange
    List<LeaveRequestDTO> expected = List.of();

    when(facade.findLeaveRequestsByStaffId("0001"))
        .thenReturn(expected);

    // Act
    Iterable<LeaveRequestDTO> result = leaveController
        .getLeaveRequestsByStaffId("0001");

    // Assert
    assertSame(expected, result);

    verify(facade)
        .findLeaveRequestsByStaffId("0001");
  }

  @Test
  @DisplayName("A leave request command is passed to the facade")
  void test03() {

    // Arrange
    AddLeaveRequestCommand command = new AddLeaveRequestCommand(
        "0001",
        new DateRange(
            LocalDate.of(2026, 11, 2),
            LocalDate.of(2026, 11, 6)),
        "Annual leave");

    // Act
    leaveController.addLeaveRequest(command);

    // Assert
    verify(facade)
        .addLeaveRequest(command);
  }

  @Test
  @DisplayName("An approve leave request command is passed to the facade")
  void test04() {

    // Arrange
    ApproveLeaveRequestCommand command = new ApproveLeaveRequestCommand("1001");

    // Act
    leaveController.approveLeaveRequest(command);

    // Assert
    verify(facade)
        .approveLeaveRequest(command);
  }
}