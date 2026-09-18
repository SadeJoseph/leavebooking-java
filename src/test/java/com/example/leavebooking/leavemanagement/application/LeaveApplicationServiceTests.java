package com.example.leavebooking.leavemanagement.application;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

import com.example.leavebooking.common.events.DomainEventManager;
import com.example.leavebooking.leavemanagement.application.exceptions.LeaveRequestNotFoundException;
import com.example.leavebooking.leavemanagement.domain.DateRange;
import com.example.leavebooking.leavemanagement.infrastructure.repositories.LeaveAllowanceRepository;
import com.example.leavebooking.leavemanagement.infrastructure.repositories.LeaveRequestRepository;
import com.example.leavebooking.leavemanagement.ui.commands.AddLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.ui.commands.ApproveLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.ui.commands.CancelLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.ui.commands.RejectLeaveRequestCommand;
import com.example.leavebooking.leavemanagement.application.exceptions.OverlappingLeaveRequestException;
import com.example.leavebooking.leavemanagement.domain.LeaveStatus;
import com.example.leavebooking.leavemanagement.domain.LeaveType;
import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveAllowanceJpa;
import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveRequestJpa;

@ExtendWith(MockitoExtension.class)
class LeaveApplicationServiceTests {

  @Mock
  private LeaveRequestRepository leaveRequestRepository;

  @Mock
  private LeaveAllowanceRepository leaveAllowanceRepository;

  @Mock
  private DomainEventManager domainEventManager;

  private LeaveApplicationService leaveApplicationService;

  @BeforeEach
  void setUp() {

    leaveApplicationService = new LeaveApplicationService(
        leaveRequestRepository,
        leaveAllowanceRepository,
        domainEventManager);
  }

  @Test
  @DisplayName("A valid leave request can be added")
  void test01() {

    // Arrange
    DateRange dateRange = new DateRange(
        LocalDate.of(2026, 11, 2),
        LocalDate.of(2026, 11, 6));

    AddLeaveRequestCommand command = new AddLeaveRequestCommand(
        "0001",
        dateRange,
        "Annual leave");

    // Simulate the staff member having no existing leave requests.
    when(leaveRequestRepository.findByStaffId("0001"))
        .thenReturn(List.of());

    // Act + Assert
    assertDoesNotThrow(
        () -> leaveApplicationService.addLeaveRequest(command));

    // A new request should have been persisted.
    verify(leaveRequestRepository).save(any());
  }

  @Test
  @DisplayName("Cancelling a leave request that does not exist throws an exception")
  void test02() {

    // Arrange
    CancelLeaveRequestCommand command = new CancelLeaveRequestCommand("9999");

    when(leaveRequestRepository.findById("9999"))
        .thenReturn(Optional.empty());

    // Act + Assert
    assertThrows(
        LeaveRequestNotFoundException.class,
        () -> leaveApplicationService.cancelLeaveRequest(command));

    // Nothing should be saved when the request cannot be found.
    verify(leaveRequestRepository, never()).save(any());
  }

  @Test
  @DisplayName("Approving a leave request that does not exist throws an exception")
  void test03() {

    // Arrange
    ApproveLeaveRequestCommand command = new ApproveLeaveRequestCommand("9999");

    when(leaveRequestRepository.findById("9999"))
        .thenReturn(Optional.empty());

    // Act + Assert
    assertThrows(
        LeaveRequestNotFoundException.class,
        () -> leaveApplicationService.approveLeaveRequest(command));

    verify(leaveRequestRepository, never()).save(any());
  }

  @Test
  @DisplayName("Rejecting a leave request that does not exist throws an exception")
  void test04() {

    // Arrange
    RejectLeaveRequestCommand command = new RejectLeaveRequestCommand("9999");

    when(leaveRequestRepository.findById("9999"))
        .thenReturn(Optional.empty());

    // Act + Assert
    assertThrows(
        LeaveRequestNotFoundException.class,
        () -> leaveApplicationService.rejectLeaveRequest(command));

    verify(leaveRequestRepository, never()).save(any());
  }

  @Test
  @DisplayName("An overlapping pending leave request cannot be added")
  void test05() {

    // Arrange
    DateRange existingDateRange = new DateRange(
        LocalDate.of(2026, 11, 4),
        LocalDate.of(2026, 11, 8));

    LeaveRequestJpa existingLeaveRequest = new LeaveRequestJpa();

    existingLeaveRequest.setId("1001");
    existingLeaveRequest.setStaffId("0001");
    existingLeaveRequest.setDateRange(existingDateRange);
    existingLeaveRequest.setReason("Existing leave");
    existingLeaveRequest.setLeaveType(
        LeaveType.ANNUAL_LEAVE.ordinal());
    existingLeaveRequest.setLeaveStatus(
        LeaveStatus.PENDING.ordinal());
    existingLeaveRequest.setDescriptionOfStatus(
        LeaveStatus.PENDING.name());

    AddLeaveRequestCommand command = new AddLeaveRequestCommand(
        "0001",
        new DateRange(
            LocalDate.of(2026, 11, 2),
            LocalDate.of(2026, 11, 6)),
        "New annual leave");

    // Simulate an existing request belonging to this staff member.
    when(leaveRequestRepository.findByStaffId("0001"))
        .thenReturn(List.of(existingLeaveRequest));

    // Act + Assert
    assertThrows(
        OverlappingLeaveRequestException.class,
        () -> leaveApplicationService.addLeaveRequest(command));

    // Nothing should be saved because the dates overlap.
    verify(leaveRequestRepository, never()).save(any());
  }

  @Test
  @DisplayName("A pending leave request can be approved and its event is published")
  void test06() {

    // Arrange
    LeaveRequestJpa pendingLeaveRequest = new LeaveRequestJpa();

    pendingLeaveRequest.setId("1001");
    pendingLeaveRequest.setStaffId("0001");

    pendingLeaveRequest.setDateRange(
        new DateRange(
            LocalDate.of(2026, 11, 2),
            LocalDate.of(2026, 11, 6)));

    pendingLeaveRequest.setReason("Annual leave");

    pendingLeaveRequest.setLeaveType(
        LeaveType.ANNUAL_LEAVE.ordinal());

    pendingLeaveRequest.setLeaveStatus(
        LeaveStatus.PENDING.ordinal());

    pendingLeaveRequest.setDescriptionOfStatus(
        LeaveStatus.PENDING.name());

    ApproveLeaveRequestCommand command = new ApproveLeaveRequestCommand("1001");

    when(leaveRequestRepository.findById("1001"))
        .thenReturn(Optional.of(pendingLeaveRequest));

    // Capture the LeaveRequestJpa passed to save().
    ArgumentCaptor<LeaveRequestJpa> leaveRequestCaptor = ArgumentCaptor.forClass(LeaveRequestJpa.class);

    // Act
    leaveApplicationService.approveLeaveRequest(command);

    // Assert
    verify(leaveRequestRepository)
        .save(leaveRequestCaptor.capture());

    LeaveRequestJpa savedLeaveRequest = leaveRequestCaptor.getValue();

    assertEquals(
        LeaveStatus.APPROVED.ordinal(),
        savedLeaveRequest.getLeaveStatus());

    assertEquals(
        "Leave request approved",
        savedLeaveRequest.getDescriptionOfStatus());
    // appproval raise domain event and it should be managed by the domain event
    // manager
    verify(domainEventManager)
        .manageDomainEvents(
            eq("LeaveApplicationService"),
            anyList());
  }

  @Test
  @DisplayName("An approved leave request can be cancelled and its event is published")
  void test07() {

    // Arrange
    LeaveRequestJpa approvedLeaveRequest = new LeaveRequestJpa();

    approvedLeaveRequest.setId("1001");
    approvedLeaveRequest.setStaffId("0001");

    approvedLeaveRequest.setDateRange(
        new DateRange(
            LocalDate.of(2026, 11, 2),
            LocalDate.of(2026, 11, 6)));

    approvedLeaveRequest.setReason("Annual leave");

    approvedLeaveRequest.setLeaveType(
        LeaveType.ANNUAL_LEAVE.ordinal());

    approvedLeaveRequest.setLeaveStatus(
        LeaveStatus.APPROVED.ordinal());

    approvedLeaveRequest.setDescriptionOfStatus(
        "Leave request approved");

    CancelLeaveRequestCommand command = new CancelLeaveRequestCommand("1001");

    when(leaveRequestRepository.findById("1001"))
        .thenReturn(Optional.of(approvedLeaveRequest));

    ArgumentCaptor<LeaveRequestJpa> leaveRequestCaptor = ArgumentCaptor.forClass(LeaveRequestJpa.class);

    // Act
    leaveApplicationService.cancelLeaveRequest(command);

    // Assert
    verify(leaveRequestRepository)
        .save(leaveRequestCaptor.capture());

    LeaveRequestJpa savedLeaveRequest = leaveRequestCaptor.getValue();

    assertEquals(
        LeaveStatus.CANCELLED.ordinal(),
        savedLeaveRequest.getLeaveStatus());

    // Cancelling raises a domain event, so it should be passed to the event
    // manager.
    verify(domainEventManager)
        .manageDomainEvents(
            eq("LeaveApplicationService"),
            anyList());
  }

  @Test
  @DisplayName("A pending leave request can be rejected")
  void test08() {

    // Arrange
    LeaveRequestJpa pendingLeaveRequest = new LeaveRequestJpa();

    pendingLeaveRequest.setId("1001");
    pendingLeaveRequest.setStaffId("0001");

    pendingLeaveRequest.setDateRange(
        new DateRange(
            LocalDate.of(2026, 11, 2),
            LocalDate.of(2026, 11, 6)));

    pendingLeaveRequest.setReason("Annual leave");

    pendingLeaveRequest.setLeaveType(
        LeaveType.ANNUAL_LEAVE.ordinal());

    pendingLeaveRequest.setLeaveStatus(
        LeaveStatus.PENDING.ordinal());

    pendingLeaveRequest.setDescriptionOfStatus(
        LeaveStatus.PENDING.name());

    RejectLeaveRequestCommand command = new RejectLeaveRequestCommand("1001");

    when(leaveRequestRepository.findById("1001"))
        .thenReturn(Optional.of(pendingLeaveRequest));

    ArgumentCaptor<LeaveRequestJpa> leaveRequestCaptor = ArgumentCaptor.forClass(LeaveRequestJpa.class);

    // Act
    leaveApplicationService.rejectLeaveRequest(command);

    // Assert
    verify(leaveRequestRepository)
        .save(leaveRequestCaptor.capture());

    LeaveRequestJpa savedLeaveRequest = leaveRequestCaptor.getValue();

    assertEquals(
        LeaveStatus.REJECTED.ordinal(),
        savedLeaveRequest.getLeaveStatus());

    // Reject currently does not publish a domain event.
    verify(domainEventManager, never())
        .manageDomainEvents(
            any(),
            anyList());
  }

  @Test
  @DisplayName("A rejected leave request does not prevent the same dates being requested again")
  void test09() {

    // Arrange
    DateRange dateRange = new DateRange(
        LocalDate.of(2026, 11, 2),
        LocalDate.of(2026, 11, 6));

    LeaveRequestJpa rejectedLeaveRequest = new LeaveRequestJpa();

    rejectedLeaveRequest.setId("1001");
    rejectedLeaveRequest.setStaffId("0001");
    rejectedLeaveRequest.setDateRange(dateRange);
    rejectedLeaveRequest.setLeaveStatus(
        LeaveStatus.REJECTED.ordinal());

    AddLeaveRequestCommand command = new AddLeaveRequestCommand(
        "0001",
        dateRange,
        "New annual leave");

    when(leaveRequestRepository.findByStaffId("0001"))
        .thenReturn(List.of(rejectedLeaveRequest));

    // Act + Assert
    assertDoesNotThrow(
        () -> leaveApplicationService.addLeaveRequest(command));

    // The previous rejected request should not block the new one.
    verify(leaveRequestRepository).save(any());
  }

  @Test
  @DisplayName("A cancelled leave request does not prevent the same dates being requested again")
  void test10() {

    // Arrange
    DateRange dateRange = new DateRange(
        LocalDate.of(2026, 11, 2),
        LocalDate.of(2026, 11, 6));

    LeaveRequestJpa cancelledLeaveRequest = new LeaveRequestJpa();

    cancelledLeaveRequest.setId("1001");
    cancelledLeaveRequest.setStaffId("0001");
    cancelledLeaveRequest.setDateRange(dateRange);
    cancelledLeaveRequest.setLeaveStatus(
        LeaveStatus.CANCELLED.ordinal());

    AddLeaveRequestCommand command = new AddLeaveRequestCommand(
        "0001",
        dateRange,
        "New annual leave");

    when(leaveRequestRepository.findByStaffId("0001"))
        .thenReturn(List.of(cancelledLeaveRequest));

    // Act + Assert
    assertDoesNotThrow(
        () -> leaveApplicationService.addLeaveRequest(command));

    // The previous cancelled request should not block the new one.
    verify(leaveRequestRepository).save(any());
  }

  @Test
  @DisplayName("A new staff member is given a default leave allowance")
  void test11() {

    // Arrange
    ArgumentCaptor<LeaveAllowanceJpa> leaveAllowanceCaptor = ArgumentCaptor.forClass(LeaveAllowanceJpa.class);

    // Act
    leaveApplicationService.addLeaveAllowance(
        "0004",
        "Becky",
        "Taylor",
        "0003");

    // Assert
    verify(leaveAllowanceRepository)
        .save(leaveAllowanceCaptor.capture());

    LeaveAllowanceJpa savedLeaveAllowance = leaveAllowanceCaptor.getValue();

    assertEquals("0004", savedLeaveAllowance.getStaffId());

    assertEquals("0003", savedLeaveAllowance.getManagerId());

    assertEquals(25, savedLeaveAllowance.getYearlyEntitlement());

    assertEquals(25, savedLeaveAllowance.getRemainingBalance());
  }
}