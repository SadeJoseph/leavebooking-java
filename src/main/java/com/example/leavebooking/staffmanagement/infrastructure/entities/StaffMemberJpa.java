package com.example.leavebooking.staffmanagement.infrastructure.entities;

import com.example.leavebooking.common.FullName;
import com.example.leavebooking.staffmanagement.domain.EmailAddress;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// This class represents how staff data is stored in the database.
@Entity(name = "staff_member")
@Table(name = "staff_member")

// JPA entities use normal getters/setters.
@Getter
@Setter
@ToString
public class StaffMemberJpa {

    // Database primary key.
    @Id
    @Column(name = "id")
    private String id;

    // FullName is a record Value Object marked as @Embeddable, so its fields can be stored as part of this table.
    @Embedded
    @Valid
    private FullName fullName;
                                                       
    // EmailAddress is also an embeddable Value Object.
    @Embedded
    @Valid
    private EmailAddress emailAddress;
}