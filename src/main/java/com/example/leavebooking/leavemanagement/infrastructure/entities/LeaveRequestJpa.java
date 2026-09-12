package com.example.leavebooking.leavemanagement.infrastructure.entities;

import com.example.leavebooking.leavemanagement.domain.DateRange;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// represents how leave request data is stored in the database.
@Entity(name = "leave_request")
@Table(name = "leave_request")
@Getter
@Setter
@ToString
public class LeaveRequestJpa {

    // Primary key for the leave request.
    @Id
    @Column(name = "id")
    private String id;


    // Reference to the StaffMember that created the request.-staff id stored not whole staff member object
    @NotBlank(message = "Staff id is required")
    @Column(name = "staff_id")
    private String staffId;


    // DateRange is an immutable Value Object marked as @Embeddable,so its start/end dates can be stored as part of this table.
    @Embedded
    @Valid
    private DateRange dateRange;


    @NotBlank(message = "Reason is required")
    @Column(name = "reason")
    private String reason;

    // so ANNUAL_LEAVE is currently ordinal 0.
    @Column(name = "leave_type")
    private int leaveType;


    // Stores the ordinal value of LeaveStatus.
    @Column(name = "leave_status")
    private int leaveStatus;


    // human readable version of the leave status
    @Column(name = "description_of_status")
    private String descriptionOfStatus;
}
