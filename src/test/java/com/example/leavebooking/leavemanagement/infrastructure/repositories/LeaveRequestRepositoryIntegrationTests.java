package com.example.leavebooking.leavemanagement.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.leavebooking.leavemanagement.domain.DateRange;
import com.example.leavebooking.leavemanagement.domain.LeaveStatus;
import com.example.leavebooking.leavemanagement.domain.LeaveType;
import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveRequestJpa;

@DataJpaTest
@DisplayName("LeaveRequestRepository Integration Tests")
class LeaveRequestRepositoryIntegrationTests {

  @Autowired
  private LeaveRequestRepository leaveRequestRepository;

  @Test
  @DisplayName("A saved leave request can be retrieved by staff id")
  void test01() {

    // Arrange
    LeaveRequestJpa leaveRequest = new LeaveRequestJpa();

    leaveRequest.setId("INT-1001");
    leaveRequest.setStaffId("INT-0001");

    leaveRequest.setDateRange(
        new DateRange(
            LocalDate.of(2026, 12, 7),
            LocalDate.of(2026, 12, 11)));

    leaveRequest.setReason("Integration test leave");

    leaveRequest.setLeaveType(
        LeaveType.ANNUAL_LEAVE.ordinal());

    leaveRequest.setLeaveStatus(
        LeaveStatus.PENDING.ordinal());

    leaveRequest.setDescriptionOfStatus(
        "Leave request pending");

    // Act
    leaveRequestRepository.save(leaveRequest);

    List<LeaveRequestJpa> results = leaveRequestRepository
        .findByStaffId("INT-0001");

    // Assert
    assertFalse(results.isEmpty());
    assertEquals(1, results.size());

    assertEquals(
        "INT-1001",
        results.getFirst().getId());

    assertEquals(
        LeaveStatus.PENDING.ordinal(),
        results.getFirst().getLeaveStatus());
  }
}