package com.example.leavebooking.leavemanagement.application;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.leavebooking.leavemanagement.infrastructure.repositories.LeaveAllowanceRepository;
import com.example.leavebooking.leavemanagement.infrastructure.repositories.LeaveRequestRepository;
import com.google.firebase.auth.FirebaseToken;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LeaveAuthorisationService {

  private final LeaveAllowanceRepository leaveAllowanceRepository;
  private final LeaveRequestRepository leaveRequestRepository;

  public boolean canAccessStaff(
      String requestedStaffId,
      Authentication authentication) {

    // Admin has company-wide access.
    if (hasRole(authentication, "ROLE_ADMIN")) {
      return true;
    }

    String loggedInStaffId = getLoggedInStaffId(authentication);

    // Normal staff can only access their own leave information.
    if (hasRole(authentication, "ROLE_USER")) {
      return requestedStaffId.equals(loggedInStaffId);
    }

    // A manager can access their own information.
    if (hasRole(authentication, "ROLE_MANAGER")
        && requestedStaffId.equals(loggedInStaffId)) {
      return true;
    }

    // A manager can access staff assigned to them.
    if (hasRole(authentication, "ROLE_MANAGER")) {

      var allowance = leaveAllowanceRepository
          .findByStaffId(requestedStaffId)
          .orElse(null);

      return allowance != null
          && loggedInStaffId.equals(
              allowance.getManagerId());
    }

    return false;
  }

  public boolean canAccessManagerTeam(
      String requestedManagerId,
      Authentication authentication) {

    // Admin can access any manager's team.
    if (hasRole(authentication, "ROLE_ADMIN")) {
      return true;
    }

    // Manager can only access their own team.
    return hasRole(authentication, "ROLE_MANAGER")
        && requestedManagerId.equals(
            getLoggedInStaffId(authentication));
  }

  public boolean canApproveOrReject(
      String leaveRequestId,
      Authentication authentication) {

    // Admin can approve/reject any request.
    if (hasRole(authentication, "ROLE_ADMIN")) {
      return true;
    }

    // Only managers can continue beyond this point.
    if (!hasRole(authentication, "ROLE_MANAGER")) {
      return false;
    }

    String loggedInManagerId = getLoggedInStaffId(authentication);

    // Find the leave request to discover which staff member owns it.
    var leaveRequest = leaveRequestRepository
        .findById(leaveRequestId)
        .orElse(null);

    if (leaveRequest == null) {
      return false;
    }

    // Find that staff member's allowance,which contains their assigned manager ID.
    var allowance = leaveAllowanceRepository
        .findByStaffId(
            leaveRequest.getStaffId())
        .orElse(null);

    if (allowance == null) {
      return false;
    }

    // Manager may only act if they are the assigned manager.
    return loggedInManagerId.equals(
        allowance.getManagerId());
  }

  private String getLoggedInStaffId(
      Authentication authentication) {

    FirebaseToken firebaseToken = (FirebaseToken) authentication.getDetails();

    return (String) firebaseToken
        .getClaims()
        .get("staffId");
  }

  private boolean hasRole(
      Authentication authentication,
      String role) {

    return authentication
        .getAuthorities()
        .stream()
        .anyMatch(
            authority -> authority.getAuthority()
                .equals(role));
  }

  public boolean canAddLeave(
      String requestedStaffId,
      Authentication authentication) {

    // Admin can submit leave for any staff member.
    if (hasRole(authentication, "ROLE_ADMIN")) {
      return true;
    }

    String loggedInStaffId = getLoggedInStaffId(authentication);

    // USER or MANAGER can only submit leave for themselves.
    return (hasRole(authentication, "ROLE_USER")
        || hasRole(authentication, "ROLE_MANAGER"))
        && requestedStaffId.equals(loggedInStaffId);
  }

  public boolean canCancelLeave(
      String leaveRequestId,
      Authentication authentication) {

    // Admin can cancel any request.
    if (hasRole(authentication, "ROLE_ADMIN")) {
      return true;
    }

    String loggedInStaffId = getLoggedInStaffId(authentication);

    // Find the leave request so we know who owns it.
    var leaveRequest = leaveRequestRepository
        .findById(leaveRequestId)
        .orElse(null);

    if (leaveRequest == null) {
      return false;
    }

    // USER or MANAGER can only cancel their own request.
    return (hasRole(authentication, "ROLE_USER")
        || hasRole(authentication, "ROLE_MANAGER"))
        && loggedInStaffId.equals(
            leaveRequest.getStaffId());
  }
}