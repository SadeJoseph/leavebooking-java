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
        new EmailAddress(event.email()));

    staffMemberRepository.save(
        StaffMemberDomainToJpaMapper.map(staffMember));

    log.info(
        "New staff member {} added from remote event",
        event.staffId());
  }
}