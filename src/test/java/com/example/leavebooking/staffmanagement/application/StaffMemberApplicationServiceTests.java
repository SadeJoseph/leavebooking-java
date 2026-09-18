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
import java.util.Optional;

import com.example.leavebooking.common.domain.FullName;
import com.example.leavebooking.staffmanagement.domain.EmailAddress;
import com.example.leavebooking.staffmanagement.infrastructure.entities.StaffMemberJpa;
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

    when(newStaffMemberAddedEvent.department())
        .thenReturn("Engineering");

    // Act
    staffMemberApplicationService
        .addNewStaffMember(newStaffMemberAddedEvent);

    // Assert
    verify(staffMemberRepository)
        .save(any());
  }

  @Test
  @DisplayName("A staff member department can be updated")
  void test02() {

    // Arrange
    StaffMemberJpa staffMember = new StaffMemberJpa();

    staffMember.setId("0001");
    staffMember.setFullName(
        new FullName(
            "Sade",
            "Joseph"));
    staffMember.setEmailAddress(
        new EmailAddress(
            "sade.joseph@example.com"));
    staffMember.setDepartment(
        "Engineering");

    when(staffMemberRepository.findById("0001"))
        .thenReturn(Optional.of(staffMember));

    // Act
    staffMemberApplicationService
        .updateDepartment(
            "0001",
            "Finance");

    // Assert
    verify(staffMemberRepository)
        .save(any());
  }
}