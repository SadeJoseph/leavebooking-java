package com.example.leavebooking.staffmanagement.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.leavebooking.common.domain.FullName;
import com.example.leavebooking.staffmanagement.domain.EmailAddress;
import com.example.leavebooking.staffmanagement.infrastructure.entities.StaffMemberJpa;

@DataJpaTest
@DisplayName("StaffMemberRepository Integration Tests")
class StaffMemberRepositoryIntegrationTests {

  @Autowired
  private StaffMemberRepository staffMemberRepository;

  @Test
  @DisplayName("A saved staff member can be retrieved by id")
  void test01() {

    // Arrange
    StaffMemberJpa staffMember = new StaffMemberJpa();

    staffMember.setId("INT-0004");
    staffMember.setFullName(
        new FullName("Sade", "Joseph"));
    staffMember.setEmailAddress(
        new EmailAddress("sade.joseph@example.com"));
    staffMember.setDepartment("Engineering");

    // Act
    staffMemberRepository.save(staffMember);

    Optional<StaffMemberJpa> result = staffMemberRepository.findById("INT-0004");

    // Assert
    assertTrue(result.isPresent());

    assertEquals(
        "INT-0004",
        result.get().getId());

    assertEquals(
        new FullName("Sade", "Joseph"),
        result.get().getFullName());

    assertEquals(
        new EmailAddress("sade.joseph@example.com"),
        result.get().getEmailAddress());

    assertEquals(
        "Engineering",
        result.get().getDepartment());
  }
}