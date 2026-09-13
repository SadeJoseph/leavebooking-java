package com.example.leavebooking.leavemanagement.infrastructure.repositories;

import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveRequestJpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository
        extends CrudRepository<LeaveRequestJpa, String> {
 // This returns every leave request belonging to one staff member.
    List<LeaveRequestJpa> findByStaffId(String staffId);
}