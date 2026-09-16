package com.example.leavebooking.staffmanagement.application.events;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.leavebooking.common.events.NewStaffMemberAddedEvent;
import com.example.leavebooking.staffmanagement.application.StaffMemberApplicationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
@RabbitListener(queues = "newStaffMember")
public class NewStaffMemberAddedListener {

    private final StaffMemberApplicationService staffMemberApplicationService;

    @RabbitHandler
    public void receive(NewStaffMemberAddedEvent event) {

        try {
            log.info(
                    "NewStaffMemberAddedListener received event \n{}",
                    event
            );

            staffMemberApplicationService.addNewStaffMember(event);

        } catch (Exception exception) {
            log.error(
                    "Failed to process NewStaffMemberAddedEvent",
                    exception
            );
        }
    }
}
