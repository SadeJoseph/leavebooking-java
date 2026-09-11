package com.example.leavebooking.staffmanagement.infrastructure.repositories;

import com.example.leavebooking.staffmanagement.infrastructure.entities.StaffMemberJpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// Repository provides database access for StaffMemberJpa entities.
@Repository
public interface StaffMemberRepository
        extends CrudRepository<StaffMemberJpa, String> {
}