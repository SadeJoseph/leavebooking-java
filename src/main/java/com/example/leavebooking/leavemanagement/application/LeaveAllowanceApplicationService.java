package com.example.leavebooking.leavemanagement.application;

import com.example.leavebooking.leavemanagement.application.exceptions.LeaveAllowanceNotFoundException;
import com.example.leavebooking.leavemanagement.application.mappers.LeaveAllowanceDomainToJpaMapper;
import com.example.leavebooking.leavemanagement.application.mappers.LeaveAllowanceJpaToDomainMapper;
import com.example.leavebooking.leavemanagement.domain.LeaveAllowance;
import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveAllowanceJpa;
import com.example.leavebooking.leavemanagement.infrastructure.repositories.LeaveAllowanceRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LeaveAllowanceApplicationService {

  private final LeaveAllowanceRepository leaveAllowanceRepository;

  @Transactional
  public void useLeave(String staffId, int days) {

    // Find the persisted allowance belonging to the staff member.
    LeaveAllowanceJpa leaveAllowanceJpa = leaveAllowanceRepository.findByStaffId(staffId)
        .orElseThrow(() -> new LeaveAllowanceNotFoundException(staffId));

    // Reconstruct the domain aggregate.
    LeaveAllowance leaveAllowance = LeaveAllowanceJpaToDomainMapper.map(
        leaveAllowanceJpa);


    leaveAllowance.useLeave(days);

   
    leaveAllowanceRepository.save(
        LeaveAllowanceDomainToJpaMapper.map(
            leaveAllowance));
  }
}