package com.example.leavebooking.staffmanagement.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
import com.example.leavebooking.staffmanagement.application.dto.StaffMemberDTO;
import com.example.leavebooking.staffmanagement.application.exceptions.StaffMemberNotFoundException;
import com.example.leavebooking.staffmanagement.domain.EmailAddress;
import com.example.leavebooking.staffmanagement.infrastructure.entities.StaffMemberJpa;
import com.example.leavebooking.staffmanagement.infrastructure.repositories.StaffMemberRepository;

@ExtendWith(MockitoExtension.class)
class StaffMemberQueryHandlerTests {

  @Mock
  private StaffMemberRepository staffMemberRepository;

  private StaffMemberQueryHandler staffMemberQueryHandler;

  @BeforeEach
  void setUp() {
    staffMemberQueryHandler = new StaffMemberQueryHandler(
        staffMemberRepository);
  }

  @Test
  @DisplayName("All staff members can be retrieved")
  void test01() {

    // Arrange
    StaffMemberJpa staffMember = getValidStaffMember();

    when(staffMemberRepository.findAll())
        .thenReturn(List.of(staffMember));

    // Act
    Iterable<StaffMemberDTO> result = staffMemberQueryHandler.findAllStaffMembers();

    List<StaffMemberDTO> results = StreamSupport
        .stream(
            result.spliterator(),
            false)
        .toList();

    // Assert
    assertEquals(1, results.size());
  }

  @Test
  @DisplayName("An exception is thrown when a staff member cannot be found")
  void test02() {

    // Arrange
    when(staffMemberRepository.findById("9999"))
        .thenReturn(Optional.empty());

    // Act + Assert
    assertThrows(
        StaffMemberNotFoundException.class,
        () -> staffMemberQueryHandler
            .findStaffMemberById("9999"));
  }


  private StaffMemberJpa getValidStaffMember() {

    StaffMemberJpa staffMember = new StaffMemberJpa();

    staffMember.setId("0001");

    staffMember.setFullName(
        new FullName(
            "Sade",
            "Joseph"));

    staffMember.setEmailAddress(
        new EmailAddress(
            "sade.joseph@example.com"));

    return staffMember;
  }
}