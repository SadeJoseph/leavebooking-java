package com.example.leavebooking.leavemanagement.domain;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

import com.example.leavebooking.common.ValueObject;

// Represents the period covered by a leave request
// A DateRange has no identity and is immutable, so it is a Value Object
@Embeddable
public record DateRange(LocalDate startDate, LocalDate endDate) implements ValueObject {

    public static final String START_DATE_CANNOT_BE_NULL = "Start date cannot be null";
    public static final String END_DATE_CANNOT_BE_NULL = "End date cannot be null";
    public static final String END_DATE_CANNOT_BE_BEFORE_START_DATE = "End date cannot be before start date";
    public static final String DATE_RANGE_CANNOT_BE_NULL = "Date range to copy cannot be null";

    // Compact constructor protects the rules of a valid date range
    public DateRange {
        if (startDate == null) {
            throw new IllegalArgumentException(
                    START_DATE_CANNOT_BE_NULL);
        }
        if (endDate == null) {
            throw new IllegalArgumentException(
                    END_DATE_CANNOT_BE_NULL);
        }
        // Same-day leave is allowed.
        // Only an end date BEFORE the start date is invalid.
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    END_DATE_CANNOT_BE_BEFORE_START_DATE);
        }
    }

    public DateRange(DateRange dateRange) {
        if (dateRange == null) {
            throw new IllegalArgumentException(
                    DATE_RANGE_CANNOT_BE_NULL);
        }
        this(dateRange.startDate, dateRange.endDate);
    }
}
