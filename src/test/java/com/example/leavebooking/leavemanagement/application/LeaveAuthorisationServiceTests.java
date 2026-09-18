package com.example.leavebooking.leavemanagement.application;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveAllowanceJpa;
import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveRequestJpa;
import com.example.leavebooking.leavemanagement.infrastructure.repositories.LeaveAllowanceRepository;
import com.example.leavebooking.leavemanagement.infrastructure.repositories.LeaveRequestRepository;
import com.google.firebase.auth.FirebaseToken;

@ExtendWith(MockitoExtension.class)
class LeaveAuthorisationServiceTests {

  @Mock
  private LeaveAllowanceRepository leaveAllowanceRepository;

  @Mock
  private LeaveRequestRepository leaveRequestRepository;

  private LeaveAuthorisationService leaveAuthorisationService;

  @BeforeEach
  void setUp() {

    leaveAuthorisationService = new LeaveAuthorisationService(
        leaveAllowanceRepository,
        leaveRequestRepository);
  }

  @Test
  @DisplayName("Admin can access any staff member")
  void test01() {

    // Arrange
    Authentication authentication = authentication("ROLE_ADMIN", "ADMIN");

    // Act
    boolean authorised = leaveAuthorisationService.canAccessStaff(
        "0001",
        authentication);

    // Assert
    assertTrue(authorised);
  }

  @Test
  @DisplayName("A user can only access their own leave information")
  void test02() {

    // Arrange
    Authentication authentication = authentication("ROLE_USER", "0001");

    // Act + Assert
    assertTrue(
        leaveAuthorisationService.canAccessStaff(
            "0001",
            authentication));

    assertFalse(
        leaveAuthorisationService.canAccessStaff(
            "0002",
            authentication));
  }

  @Test
  @DisplayName("A manager can access leave information for staff assigned to them")
  void test03() {

    // Arrange
    Authentication authentication = authentication("ROLE_MANAGER", "0003");

    LeaveAllowanceJpa allowance = allowance("0001", "0003");

    when(leaveAllowanceRepository.findByStaffId("0001"))
        .thenReturn(Optional.of(allowance));

    // Act
    boolean authorised = leaveAuthorisationService.canAccessStaff(
        "0001",
        authentication);

    // Assert
    assertTrue(authorised);
  }

  @Test
  @DisplayName("A manager cannot access staff assigned to another manager")
  void test04() {

    // Arrange
    Authentication authentication = authentication("ROLE_MANAGER", "0003");

    LeaveAllowanceJpa allowance = allowance("0001", "0004");

    when(leaveAllowanceRepository.findByStaffId("0001"))
        .thenReturn(Optional.of(allowance));

    // Act
    boolean authorised = leaveAuthorisationService.canAccessStaff(
        "0001",
        authentication);

    // Assert
    assertFalse(authorised);
  }

  @Test
  @DisplayName("A manager can only access their own team")
  void test05() {

    // Arrange
    Authentication authentication = authentication("ROLE_MANAGER", "0003");

    // Act + Assert
    assertTrue(
        leaveAuthorisationService.canAccessManagerTeam(
            "0003",
            authentication));

    assertFalse(
        leaveAuthorisationService.canAccessManagerTeam(
            "0004",
            authentication));
  }

  @Test
  @DisplayName("A user can only add leave for themselves")
  void test06() {

    // Arrange
    Authentication authentication = authentication("ROLE_USER", "0001");

    // Act + Assert
    assertTrue(
        leaveAuthorisationService.canAddLeave(
            "0001",
            authentication));

    assertFalse(
        leaveAuthorisationService.canAddLeave(
            "0002",
            authentication));
  }

  @Test
  @DisplayName("A user can only cancel their own leave request")
  void test07() {

    // Arrange
    Authentication authentication = authentication("ROLE_USER", "0001");

    LeaveRequestJpa ownRequest = leaveRequest("1001", "0001");

    LeaveRequestJpa anotherStaffRequest = leaveRequest("1002", "0002");

    when(leaveRequestRepository.findById("1001"))
        .thenReturn(Optional.of(ownRequest));

    when(leaveRequestRepository.findById("1002"))
        .thenReturn(Optional.of(anotherStaffRequest));

    // Act + Assert
    assertTrue(
        leaveAuthorisationService.canCancelLeave(
            "1001",
            authentication));

    assertFalse(
        leaveAuthorisationService.canCancelLeave(
            "1002",
            authentication));
  }

  @Test
  @DisplayName("A manager can approve or reject leave for staff assigned to them")
  void test08() {

    // Arrange
    Authentication authentication = authentication("ROLE_MANAGER", "0003");

    LeaveRequestJpa leaveRequest = leaveRequest("1001", "0001");

    LeaveAllowanceJpa allowance = allowance("0001", "0003");

    when(leaveRequestRepository.findById("1001"))
        .thenReturn(Optional.of(leaveRequest));

    when(leaveAllowanceRepository.findByStaffId("0001"))
        .thenReturn(Optional.of(allowance));

    // Act
    boolean authorised = leaveAuthorisationService.canApproveOrReject(
        "1001",
        authentication);

    // Assert
    assertTrue(authorised);
  }

  @Test
  @DisplayName("A manager cannot approve or reject leave for staff assigned to another manager")
  void test09() {

    // Arrange
    Authentication authentication = authentication("ROLE_MANAGER", "0003");

    LeaveRequestJpa leaveRequest = leaveRequest("1001", "0001");

    LeaveAllowanceJpa allowance = allowance("0001", "0004");

    when(leaveRequestRepository.findById("1001"))
        .thenReturn(Optional.of(leaveRequest));

    when(leaveAllowanceRepository.findByStaffId("0001"))
        .thenReturn(Optional.of(allowance));

    // Act
    boolean authorised = leaveAuthorisationService.canApproveOrReject(
        "1001",
        authentication);

    // Assert
    assertFalse(authorised);
  }

  private Authentication authentication(
      String role,
      String staffId) {

    FirebaseToken firebaseToken = mock(FirebaseToken.class);

    // Admin methods return before the staffId claim is needed
    if (!role.equals("ROLE_ADMIN")) {
      when(firebaseToken.getClaims())
          .thenReturn(
              Map.of("staffId", staffId));
    }

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        "firebase-user",
        null,
        List.of(
            new SimpleGrantedAuthority(role)));

    authentication.setDetails(firebaseToken);

    return authentication;
  }

  // Creates the minimum LeaveAllowanceJpa required for the authorisation tests.
  private LeaveAllowanceJpa allowance(
      String staffId,
      String managerId) {

    LeaveAllowanceJpa allowance = new LeaveAllowanceJpa();

    allowance.setStaffId(staffId);
    allowance.setManagerId(managerId);

    return allowance;
  }

  // Creates the minimum LeaveRequestJpa required for ownership checks.

  private LeaveRequestJpa leaveRequest(
      String id,
      String staffId) {

    LeaveRequestJpa leaveRequest = new LeaveRequestJpa();

    leaveRequest.setId(id);
    leaveRequest.setStaffId(staffId);

    return leaveRequest;
  }
}