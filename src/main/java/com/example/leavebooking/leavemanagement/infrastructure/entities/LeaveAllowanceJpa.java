package com.example.leavebooking.leavemanagement.infrastructure.entities;

import com.example.leavebooking.common.FullName;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "leave_allowance")
@Table(name = "leave_allowance")
@Getter
@Setter
@ToString
public class LeaveAllowanceJpa {

  // Primary key for this LeaveAllowance record.
  @Id
  @Column(name = "id")
  private String id;

  // Identity of the staff member
  @NotBlank(message = "Staff id is required")
  @Column(name = "staff_id")
  private String staffId;

  @Embedded
  @Valid
  private FullName staffName;

  // Identity of the staff member's line manager.
  @NotBlank(message = "Manager id is required")
  @Column(name = "manager_id")
  private String managerId;

  @Column(name = "yearly_entitlement")
  private int yearlyEntitlement;

  @Column(name = "remaining_balance")
  private int remainingBalance;
}