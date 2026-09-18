package com.example.leavebooking.leavemanagement;

import com.example.leavebooking.leavemanagement.application.LeaveApplicationService;
import com.example.leavebooking.leavemanagement.application.LeaveAuthorisationService;
import com.example.leavebooking.leavemanagement.application.LeaveQueryHandler;
import com.example.leavebooking.leavemanagement.application.dto.LeaveRequestDTO;
import com.example.leavebooking.leavemanagement.ui.commands.ApproveLeaveRequestCommand;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ContextFacade.class })
@DisplayName("Facade Security and unit tests")
@EnableMethodSecurity(securedEnabled = true)
class FacadeTests {

  @Autowired
  private ContextFacade facade;

  @MockitoBean
  private LeaveQueryHandler leaveQueryHandler;

  @MockitoBean
  private LeaveApplicationService leaveApplicationService;

  @MockitoBean(name = "leaveAuthorisationService")
  private LeaveAuthorisationService leaveAuthorisationService;

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("ADMIN role can access all leave requests")
  void test01() {

    // Arrange
    LeaveRequestDTO leaveRequest = org.mockito.Mockito.mock(LeaveRequestDTO.class);

    when(leaveQueryHandler.findAllLeaveRequests())
        .thenReturn(List.of(leaveRequest));

    // Act
    Iterable<LeaveRequestDTO> result = facade.findAllLeaveRequests();

    // Assert
    assertThat(result)
        .containsExactly(leaveRequest);

    verify(leaveQueryHandler)
        .findAllLeaveRequests();
  }

  @Test
  @WithMockUser(roles = "USER")
  @DisplayName("USER role cannot access all leave requests")
  void test02() {

    // Act + Assert
    assertThatThrownBy(
        () -> facade.findAllLeaveRequests())
        .isInstanceOf(
            AccessDeniedException.class);

    verifyNoInteractions(leaveQueryHandler);
  }

  @Test
  @WithMockUser(roles = "USER")
  @DisplayName("USER can access staff leave when authorised")
  void test03() {

    // Arrange
    LeaveRequestDTO leaveRequest = org.mockito.Mockito.mock(LeaveRequestDTO.class);

    when(
        leaveAuthorisationService.canAccessStaff(
            eq("0001"),
            any()))
        .thenReturn(true);

    when(
        leaveQueryHandler
            .findLeaveRequestsByStaffId("0001"))
        .thenReturn(List.of(leaveRequest));

    // Act
    Iterable<LeaveRequestDTO> result = facade.findLeaveRequestsByStaffId("0001");

    // Assert
    assertThat(result)
        .containsExactly(leaveRequest);

    verify(leaveQueryHandler)
        .findLeaveRequestsByStaffId("0001");
  }

  @Test
  @WithMockUser(roles = "USER")
  @DisplayName("USER is denied staff leave when not authorised")
  void test04() {

    // Arrange
    when(
        leaveAuthorisationService.canAccessStaff(
            eq("0002"),
            any()))
        .thenReturn(false);

    // Act + Assert
    assertThatThrownBy(
        () -> facade
            .findLeaveRequestsByStaffId("0002"))
        .isInstanceOf(
            AccessDeniedException.class);

    verifyNoInteractions(leaveQueryHandler);
  }

  @Test
  @WithMockUser(roles = "MANAGER")
  @DisplayName("MANAGER can approve leave when authorised")
  void test05() {

    // Arrange
    ApproveLeaveRequestCommand command = new ApproveLeaveRequestCommand("1001");

    when(
        leaveAuthorisationService
            .canApproveOrReject(
                eq("1001"),
                any()))
        .thenReturn(true);

    // Act + Assert
    assertThatNoException()
        .isThrownBy(
            () -> facade
                .approveLeaveRequest(command));

    verify(leaveApplicationService)
        .approveLeaveRequest(command);
  }

  @Test
  @WithMockUser(roles = "MANAGER")
  @DisplayName("MANAGER cannot approve leave when not authorised")
  void test06() {

    // Arrange
    ApproveLeaveRequestCommand command = new ApproveLeaveRequestCommand("1001");

    when(
        leaveAuthorisationService
            .canApproveOrReject(
                eq("1001"),
                any()))
        .thenReturn(false);

    // Act + Assert
    assertThatThrownBy(
        () -> facade
            .approveLeaveRequest(command))
        .isInstanceOf(
            AccessDeniedException.class);

    verifyNoInteractions(leaveApplicationService);
  }
}