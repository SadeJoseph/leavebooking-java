package com.example.leavebooking;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false)
@ToString
public class DateRange extends ValueObject {

    public static final String START_DATE_CANNOT_BE_NULL = "Start date cannot be null";
    public static final String END_DATE_CANNOT_BE_NULL = "End date cannot be null";
    public static final String END_DATE_CANNOT_BE_BEFORE_START_DATE ="End date cannot be before start date";
    public static final String DATE_RANGE_CANNOT_BE_NULL ="Date range to copy cannot be null";

    private final LocalDate startDate;
    private final LocalDate endDate;

    public DateRange(LocalDate startDate, LocalDate endDate) {

        if (startDate == null) {
            throw new IllegalArgumentException(START_DATE_CANNOT_BE_NULL);
        }

        if (endDate == null) {
            throw new IllegalArgumentException(END_DATE_CANNOT_BE_NULL);
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    END_DATE_CANNOT_BE_BEFORE_START_DATE
            );
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DateRange(DateRange dateRange) {
        if (dateRange == null) {
            throw new IllegalArgumentException(DATE_RANGE_CANNOT_BE_NULL);
        }

        this(dateRange.startDate, dateRange.endDate);
    }

    public LocalDate startDate() {
        return startDate;
    }

    public LocalDate endDate() {
        return endDate;
    }
}
