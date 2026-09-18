package com.example.leavebooking.leavemanagement.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.leavebooking.common.domain.FullName;
import com.example.leavebooking.leavemanagement.application.dto.LeaveAllowanceDTO;
import com.example.leavebooking.leavemanagement.application.dto.LeaveRequestDTO;
import com.example.leavebooking.leavemanagement.application.exceptions.LeaveAllowanceNotFoundException;
import com.example.leavebooking.leavemanagement.domain.DateRange;
import com.example.leavebooking.leavemanagement.domain.LeaveStatus;
import com.example.leavebooking.leavemanagement.domain.LeaveType;
import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveAllowanceJpa;
import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveRequestJpa;
import com.example.leavebooking.leavemanagement.infrastructure.repositories.LeaveAllowanceRepository;
import com.example.leavebooking.leavemanagement.infrastructure.repositories.LeaveRequestRepository;

@ExtendWith(MockitoExtension.class)
class LeaveQueryHandlerTests {

  @Mock
  private LeaveRequestRepository leaveRequestRepository;

  @Mock
  private LeaveAllowanceRepository leaveAllowanceRepository;

  private LeaveQueryHandler leaveQueryHandler;

  @BeforeEach
  void setUp() {

    // SUT - Subject Under Test
    leaveQueryHandler = new LeaveQueryHandler(
        leaveRequestRepository,
        leaveAllowanceRepository);
  }

  @Test
  @DisplayName("All leave requests can be retrieved")
  void test01() {

    // Arrange
    LeaveRequestJpa leaveRequest = getValidLeaveRequest(
        "1001",
        "0001",
        LeaveStatus.PENDING);

    when(leaveRequestRepository.findAll())
        .thenReturn(List.of(leaveRequest));

    // Act
    Iterable<LeaveRequestDTO> result = leaveQueryHandler.findAllLeaveRequests();

    List<LeaveRequestDTO> results = StreamSupport
        .stream(
            result.spliterator(),
            false)
        .toList();

    // Assert
    assertEquals(1, results.size());
  }

  @Test
  @DisplayName("All leave allowances can be retrieved")
  void test02() {

    // Arrange
    LeaveAllowanceJpa allowance = getValidLeaveAllowance(
        "2001",
        "0001",
        "0003");

    when(leaveAllowanceRepository.findAll())
        .thenReturn(List.of(allowance));

    // Act
    Iterable<LeaveAllowanceDTO> result = leaveQueryHandler.findAllLeaveAllowances();

    List<LeaveAllowanceDTO> results = StreamSupport
        .stream(
            result.spliterator(),
            false)
        .toList();

    // Assert
    assertEquals(1, results.size());
  }

  @Test
  @DisplayName("Leave requests can be retrieved for one staff member")
  void test03() {

    // Arrange
    LeaveRequestJpa leaveRequest = getValidLeaveRequest(
        "1001",
        "0001",
        LeaveStatus.PENDING);

    when(leaveRequestRepository.findByStaffId("0001"))
        .thenReturn(List.of(leaveRequest));

    // Act
    Iterable<LeaveRequestDTO> result = leaveQueryHandler
        .findLeaveRequestsByStaffId("0001");

    List<LeaveRequestDTO> results = StreamSupport
        .stream(
            result.spliterator(),
            false)
        .toList();

    // Assert
    assertEquals(1, results.size());
  }

  @Test
  @DisplayName("An exception is thrown when a staff member has no leave allowance")
  void test04() {

    // Arrange
    when(leaveAllowanceRepository.findByStaffId("9999"))
        .thenReturn(Optional.empty());

    // Act + Assert
    assertThrows(
        LeaveAllowanceNotFoundException.class,
        () -> leaveQueryHandler
            .findLeaveAllowanceByStaffId("9999"));
  }

  @Test
  @DisplayName("A manager only receives pending requests belonging to their assigned staff")
  void test05() {

    // Arrange
    LeaveAllowanceJpa managedStaffAllowance = getValidLeaveAllowance(
        "2001",
        "0001",
        "0003");

    when(leaveAllowanceRepository.findByManagerId("0003"))
        .thenReturn(
            List.of(managedStaffAllowance));

    LeaveRequestJpa managedStaffRequest = getValidLeaveRequest(
        "1001",
        "0001",
        LeaveStatus.PENDING);

    LeaveRequestJpa otherStaffRequest = getValidLeaveRequest(
        "1002",
        "0002",
        LeaveStatus.PENDING);

    when(
        leaveRequestRepository.findByLeaveStatus(
            LeaveStatus.PENDING.ordinal()))
        .thenReturn(
            List.of(
                managedStaffRequest,
                otherStaffRequest));

    // Act
    Iterable<LeaveRequestDTO> result = leaveQueryHandler
        .findPendingLeaveRequestsByManagerId(
            "0003");

    List<LeaveRequestDTO> results = StreamSupport
        .stream(
            result.spliterator(),
            false)
        .toList();

    // Assert
    assertEquals(1, results.size());
  }

  private LeaveRequestJpa getValidLeaveRequest(
      String id,
      String staffId,
      LeaveStatus status) {

    LeaveRequestJpa leaveRequest = new LeaveRequestJpa();

    leaveRequest.setId(id);
    leaveRequest.setStaffId(staffId);

    leaveRequest.setDateRange(
        new DateRange(
            LocalDate.of(2026, 11, 2),
            LocalDate.of(2026, 11, 6)));

    leaveRequest.setReason("Annual leave");

    leaveRequest.setLeaveType(
        LeaveType.ANNUAL_LEAVE.ordinal());

    leaveRequest.setLeaveStatus(
        status.ordinal());

    leaveRequest.setDescriptionOfStatus(
        status.name());

    return leaveRequest;
  }

  private LeaveAllowanceJpa getValidLeaveAllowance(
      String id,
      String staffId,
      String managerId) {

    LeaveAllowanceJpa allowance = new LeaveAllowanceJpa();

    allowance.setId(id);
    allowance.setStaffId(staffId);

    allowance.setStaffName(
        new FullName(
            "Sade",
            "Joseph"));

    allowance.setManagerId(managerId);
    allowance.setYearlyEntitlement(25);
    allowance.setRemainingBalance(20);

    return allowance;
  }
}