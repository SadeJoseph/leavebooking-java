package com.example.leavebooking.staffmanagement.domain;

import com.example.leavebooking.common.domain.AggregateRoot;
import com.example.leavebooking.common.domain.FullName;
import com.example.leavebooking.common.domain.Identity;

import lombok.ToString;

@ToString(callSuper = true)
public class StaffMember extends AggregateRoot<StaffMember> {

    public static final String FULL_NAME_CANNOT_BE_NULL = "Full name cannot be null";
    public static final String EMAIL_ADDRESS_CANNOT_BE_NULL = "Email address cannot be null";
    public static final String DEPARTMENT_CANNOT_BE_BLANK = "Department cannot be blank";

    private FullName fullName;

    private EmailAddress emailAddress;

    private String department;

    public StaffMember(
            Identity<StaffMember> id,
            FullName fullName,
            EmailAddress emailAddress,
            String department) {

        super(id);

        updateFullName(fullName);
        changeEmailAddress(emailAddress);
        updateDepartment(department);
    }

    public final void updateFullName(FullName fullName) {

        if (fullName == null) {
            throw new IllegalArgumentException(
                    FULL_NAME_CANNOT_BE_NULL);
        }

        this.fullName = new FullName(fullName);
    }

    public final void changeEmailAddress(
            EmailAddress emailAddress) {

        if (emailAddress == null) {
            throw new IllegalArgumentException(
                    EMAIL_ADDRESS_CANNOT_BE_NULL);
        }

        this.emailAddress = new EmailAddress(emailAddress);
    }

    public final void updateDepartment(
            String department) {

        if (department == null
                || department.isBlank()) {

            throw new IllegalArgumentException(
                    DEPARTMENT_CANNOT_BE_BLANK);
        }

        this.department = department.trim();
    }

    public Identity<StaffMember> id() {
        return id;
    }

    public FullName fullName() {
        return fullName;
    }

    public EmailAddress emailAddress() {
        return emailAddress;
    }

    public String department() {
        return department;
    }
}