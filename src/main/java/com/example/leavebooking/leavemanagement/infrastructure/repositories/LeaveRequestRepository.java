package com.example.leavebooking.leavemanagement.infrastructure.repositories;

import com.example.leavebooking.leavemanagement.infrastructure.entities.LeaveRequestJpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// Repository providing database access for LeaveRequestJpa entities.
@Repository
public interface LeaveRequestRepository
        extends CrudRepository<LeaveRequestJpa, String> {
}