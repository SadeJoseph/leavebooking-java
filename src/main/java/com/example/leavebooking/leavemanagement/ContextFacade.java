package com.example.leavebooking.leavemanagement;

import com.example.leavebooking.leavemanagement.application.LeaveQueryHandler;
import com.example.leavebooking.leavemanagement.application.dto.LeaveAllowanceDTO;
import com.example.leavebooking.leavemanagement.application.dto.LeaveRequestDTO;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

@Component("leaveManagementContextFacade")
@AllArgsConstructor
public class ContextFacade {
    private final LeaveQueryHandler leaveQueryHandler;

    public Iterable<LeaveRequestDTO> findAllLeaveRequests() {
        return leaveQueryHandler.findAllLeaveRequests();
    }

    public Iterable<LeaveAllowanceDTO> findAllLeaveAllowances() {
        return leaveQueryHandler.findAllLeaveAllowances();
    }
}
