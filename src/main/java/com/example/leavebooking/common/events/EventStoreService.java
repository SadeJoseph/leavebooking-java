package com.example.leavebooking.common.events;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
@AllArgsConstructor
public class EventStoreService {

  public enum StatusOfMessageDelivery {
    PENDING,
    PUBLISHED,
    FAILED,
    UNROUTABLE
  }

  private final EventStoreRepository eventStoreRepository;
  private final ObjectMapper objectMapper;

  @Transactional
  public EventStoreJpa append(Event event) {

    try {
      EventStoreJpa newEventJpa = new EventStoreJpa();

      newEventJpa.setId(null);
      newEventJpa.setEventType(
          event.getClass().getSimpleName());
      newEventJpa.setOccurredOn(LocalDate.now());

      // Store the event payload as JSON.
      newEventJpa.setEventBody(
          objectMapper.writeValueAsString(event));

      newEventJpa.setStatus(
          StatusOfMessageDelivery.PENDING.name());
      newEventJpa.setRetryCount(0);

      return eventStoreRepository.save(newEventJpa);

    } catch (JacksonException exception) {
      throw new IllegalArgumentException(
          "Failed to serialise event payload",
          exception);
    }
  }

  @Transactional
  public void updateStatus(
      Long eventId,
      StatusOfMessageDelivery statusOfMessageDelivery,
      boolean incrementRetryCount) {

    eventStoreRepository.findById(eventId).ifPresent(event -> {

      event.setStatus(statusOfMessageDelivery.name());

      if (incrementRetryCount) {
        event.setRetryCount(event.getRetryCount() + 1);
      }

      eventStoreRepository.save(event);

      log.error(
          "Event {} marked as {}",
          eventId,
          event.getStatus());
    });
  }
}