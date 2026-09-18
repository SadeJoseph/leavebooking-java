package com.example.leavebooking.staffmanagement.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.leavebooking.common.domain.FullName;
import com.example.leavebooking.common.domain.Identity;
import com.example.leavebooking.common.events.NewStaffMemberAddedEvent;
import com.example.leavebooking.staffmanagement.application.mappers.StaffMemberDomainToJpaMapper;
import com.example.leavebooking.staffmanagement.domain.EmailAddress;
import com.example.leavebooking.staffmanagement.domain.StaffMember;
import com.example.leavebooking.staffmanagement.infrastructure.repositories.StaffMemberRepository;
import com.example.leavebooking.staffmanagement.application.exceptions.StaffMemberNotFoundException;
import com.example.leavebooking.staffmanagement.application.mappers.StaffMemberJpaToDomainMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class StaffMemberApplicationService {

  private final StaffMemberRepository staffMemberRepository;

  @Transactional
  public void addNewStaffMember(NewStaffMemberAddedEvent event) {

    StaffMember staffMember = new StaffMember(
        Identity.of(event.staffId()),
        new FullName(
            event.firstName(),
            event.surname()),
        new EmailAddress(event.email()),
        event.department());

    staffMemberRepository.save(
        StaffMemberDomainToJpaMapper.map(staffMember));

    log.info(
        "New staff member {} added from remote event",
        event.staffId());
  }

  @Transactional
  public void addStaffMember(
      String firstName,
      String surname,
      String email,
      String department) {

    Identity<StaffMember> newStaffMemberId = Identity.generateId();

    StaffMember staffMember = new StaffMember(
        newStaffMemberId,
        new FullName(
            firstName,
            surname),
        new EmailAddress(email),
        department);

    staffMemberRepository.save(
        StaffMemberDomainToJpaMapper.map(
            staffMember));

    log.info(
        "New staff member {} added by admin",
        newStaffMemberId.id());
  }

  @Transactional
  public void updateDepartment(
      String staffId,
      String department) {

    StaffMember staffMember = staffMemberRepository
        .findById(staffId)
        .map(StaffMemberJpaToDomainMapper::map)
        .orElseThrow(
            () -> new StaffMemberNotFoundException(staffId));

    staffMember.updateDepartment(
        department);

    staffMemberRepository.save(
        StaffMemberDomainToJpaMapper
            .map(staffMember));

    log.info(
        "Department updated for staff member {}",
        staffId);
  }
}