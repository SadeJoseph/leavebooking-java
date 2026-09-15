package com.example.leavebooking.leavemanagement.application.mappers;

import com.example.leavebooking.common.Identity;
import com.example.leavebooking.leavemanagement.domain.LeaveAllowance;
import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveAllowanceJpa;

import java.util.Objects;

// Maps persisted LeaveAllowance data back into
// the LeaveAllowance domain aggregate.
public class LeaveAllowanceJpaToDomainMapper {

    public static LeaveAllowance map(
            LeaveAllowanceJpa leaveAllowanceJpa
    ) {

        Objects.requireNonNull(
                leaveAllowanceJpa,
                "Leave allowance JPA entity cannot be null"
        );

        return LeaveAllowance.leaveAllowanceOf(
                Identity.of(leaveAllowanceJpa.getId()),
                Identity.of(leaveAllowanceJpa.getStaffId()),
                leaveAllowanceJpa.getStaffName(),
                Identity.of(leaveAllowanceJpa.getManagerId()),
                leaveAllowanceJpa.getYearlyEntitlement(),
                leaveAllowanceJpa.getRemainingBalance()
        );
    }
}