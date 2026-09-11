package com.example.leavebooking.leavemanagement.domain;

import com.example.leavebooking.common.AggregateRoot;
import com.example.leavebooking.common.Entity;
import com.example.leavebooking.common.FullName;
import com.example.leavebooking.common.Identity;
import com.example.leavebooking.staffmanagement.domain.StaffMember;

import lombok.ToString;

@ToString(callSuper = true)
// an entity and also an aggregate root.
public class LeaveAllowance extends Entity<LeaveAllowance> implements AggregateRoot {

    public static final String STAFF_ID_CANNOT_BE_NULL = "Staff ID cannot be null";
    public static final String STAFF_NAME_CANNOT_BE_NULL = "Staff name cannot be null";
    public static final String MANAGER_ID_CANNOT_BE_NULL = "Manager ID cannot be null";
    public static final String ENTITLEMENT_MUST_BE_POSITIVE = "Annual leave entitlement must be greater than zero";
    public static final String DAYS_MUST_BE_POSITIVE = "Number of leave days must be greater than zero";
    public static final String INSUFFICIENT_LEAVE_BALANCE = "Insufficient annual leave remaining";
    public static final String BALANCE_CANNOT_EXCEED_ENTITLEMENT = "Remaining leave cannot exceed annual entitlement";
    public static final String ENTITLEMENT_LESS_THAN_DAYS_USED = "Annual entitlement cannot be less than leave already used";

    private final Identity<StaffMember> staffId; 

    private FullName staffName;

    private Identity<StaffMember> managerId; // mamager responsible for the staff memeber

    private int yearlyEntitlement;
    private int remainingBalance;

    public LeaveAllowance(
            Identity<LeaveAllowance> id,
            Identity<StaffMember> staffId,
            FullName staffName,
            Identity<StaffMember> managerId,
            int yearlyEntitlement) {
        super(id);

        if (staffId == null) {
            throw new IllegalArgumentException(STAFF_ID_CANNOT_BE_NULL);
        }
        if (managerId == null) {
            throw new IllegalArgumentException(MANAGER_ID_CANNOT_BE_NULL);
        }
        if (yearlyEntitlement <= 0) {
            throw new IllegalArgumentException(ENTITLEMENT_MUST_BE_POSITIVE);
        }

        this.staffId = staffId;

        updateStaffName(staffName);
        this.managerId = managerId;
        this.yearlyEntitlement = yearlyEntitlement;

        this.remainingBalance = yearlyEntitlement;
    }

    public Identity<LeaveAllowance> id() {
        return id;
    }

    public Identity<StaffMember> staffId() {
        return staffId;
    }

    public FullName staffName() {
        return staffName;
    }

    public Identity<StaffMember> managerId() {
        return managerId;
    }

    public int yearlyEntitlement() {
        return yearlyEntitlement;
    }

    public int remainingBalance() {
        return remainingBalance;
    }

    public int daysUsed() {
        return yearlyEntitlement - remainingBalance;
    }

    public void updateStaffName(FullName staffName) {
        if (staffName == null) {
            throw new IllegalArgumentException(STAFF_NAME_CANNOT_BE_NULL);
        }

        this.staffName = new FullName(staffName);
    }

    public void changeManager(Identity<StaffMember> managerId) {
        if (managerId == null) {
            throw new IllegalArgumentException(MANAGER_ID_CANNOT_BE_NULL);
        }

        this.managerId = managerId;
    }

    public void useLeave(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException(DAYS_MUST_BE_POSITIVE);
        }

        if (days > remainingBalance) {
            throw new IllegalArgumentException(INSUFFICIENT_LEAVE_BALANCE);
        }

        remainingBalance -= days;
    }

    public void restoreLeave(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException(DAYS_MUST_BE_POSITIVE);
        }

        if (remainingBalance + days > yearlyEntitlement) {
            throw new IllegalArgumentException(
                    BALANCE_CANNOT_EXCEED_ENTITLEMENT);
        }

        remainingBalance += days;
    }

    public void amendYearlyEntitlement(int newEntitlement) { // admin can amend the staff memebers yealy entitlement
        if (newEntitlement <= 0) {
            throw new IllegalArgumentException(ENTITLEMENT_MUST_BE_POSITIVE);
        }

        int daysAlreadyUsed = daysUsed();

        if (newEntitlement < daysAlreadyUsed) { // cant be set below the number of days already used by the staff member
            throw new IllegalArgumentException(
                    ENTITLEMENT_LESS_THAN_DAYS_USED);
        }

        yearlyEntitlement = newEntitlement;
        remainingBalance = newEntitlement - daysAlreadyUsed;
    }
}