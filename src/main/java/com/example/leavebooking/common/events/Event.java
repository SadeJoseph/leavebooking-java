package com.example.leavebooking.common.events;

// Base type for domain events raised by aggregates.
public interface Event {
    Long getId();

    Event withId(Long id);
}