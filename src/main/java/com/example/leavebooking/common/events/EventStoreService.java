package com.example.leavebooking.common.events;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Stores domain events in the local event store.
@Service
@Slf4j
@AllArgsConstructor
public class EventStoreService {

  private final EventStoreRepository eventsStore;

  public void append(Event event) {

    EventStoreJpa newEventJpa = new EventStoreJpa();

    // database generates the event store id.
    newEventJpa.setId(null);

    newEventJpa.setEventType(
        event.getClass().getName());

    newEventJpa.setOccurredOn(
        LocalDate.now());

    // Store a readable representation of the event.
    newEventJpa.setEventBody(
        event.toString());

    eventsStore.save(newEventJpa);

    log.info(
        "Added to event store: {}",
        newEventJpa);
  }
}