package com.example.leavebooking.staffmanagement.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.leavebooking.common.events.NewStaffMemberAddedEvent;
import com.example.leavebooking.staffmanagement.infrastructure.repositories.StaffMemberRepository;

@ExtendWith(MockitoExtension.class)
class StaffMemberApplicationServiceTests {

  @Mock
  private StaffMemberRepository staffMemberRepository;

  @Mock
  private NewStaffMemberAddedEvent newStaffMemberAddedEvent;

  private StaffMemberApplicationService staffMemberApplicationService;

  @BeforeEach
  void setUp() {

    staffMemberApplicationService = new StaffMemberApplicationService(
        staffMemberRepository);
  }

  @Test
  @DisplayName("A new staff member received from a remote event is saved")
  void test01() {

    // Arrange
    when(newStaffMemberAddedEvent.staffId())
        .thenReturn("0004");

    when(newStaffMemberAddedEvent.firstName())
        .thenReturn("Becky");

    when(newStaffMemberAddedEvent.surname())
        .thenReturn("Taylor");

    when(newStaffMemberAddedEvent.email())
        .thenReturn("becky.taylor@example.com");

    // Act
    staffMemberApplicationService
        .addNewStaffMember(newStaffMemberAddedEvent);

    // Assert
    verify(staffMemberRepository)
        .save(any());
  }
}