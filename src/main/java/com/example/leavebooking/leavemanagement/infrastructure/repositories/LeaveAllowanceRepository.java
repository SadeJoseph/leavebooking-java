package com.example.leavebooking.leavemanagement.infrastructure.repositories;

import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveAllowanceJpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveAllowanceRepository
        extends CrudRepository<LeaveAllowanceJpa, String> {
}