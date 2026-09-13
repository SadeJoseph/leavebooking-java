package com.example.leavebooking.leavemanagement.infrastructure.repositories;

import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveAllowanceJpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveAllowanceRepository
        extends CrudRepository<LeaveAllowanceJpa, String> {

          Optional<LeaveAllowanceJpa> findByStaffId(String staffId); //optional as staff memebers have one leave allowance. 
}