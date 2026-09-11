package com.example.leavebooking.staffmanagement.domain;

import com.example.leavebooking.common.AggregateRoot;
import com.example.leavebooking.common.Entity;
import com.example.leavebooking.common.FullName;
import com.example.leavebooking.common.Identity;

import lombok.ToString;

@ToString(callSuper = true)
public class StaffMember extends Entity<StaffMember> implements AggregateRoot {

    public static final String FULL_NAME_CANNOT_BE_NULL = "Full name cannot be null";
    public static final String EMAIL_ADDRESS_CANNOT_BE_NULL = "Email address cannot be null";

    private FullName fullName;
    private EmailAddress emailAddress;

    public StaffMember(
            Identity<StaffMember> id,
            FullName fullName,
            EmailAddress emailAddress) {
        super(id);
        updateFullName(fullName);
        changeEmailAddress(emailAddress);
    }

    public final void updateFullName(FullName fullName) {
        if (fullName == null) {
            throw new IllegalArgumentException(FULL_NAME_CANNOT_BE_NULL);
        }
        this.fullName = new FullName(fullName);
    }

    public final void changeEmailAddress(EmailAddress emailAddress) {
        if (emailAddress == null) {
            throw new IllegalArgumentException(EMAIL_ADDRESS_CANNOT_BE_NULL);
        }
        this.emailAddress = new EmailAddress(emailAddress);
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
}