package com.example.leavebooking.common.events;

import java.util.List;
import java.util.Objects;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class DomainEventManager {

	private final ApplicationEventPublisher eventPublisher;
	private final EventStoreService eventStoreService;

	@Transactional
	public void manageDomainEvents(
			String sourceContext,
			List<Event> events) {

		Objects.requireNonNull(
				sourceContext,
				"Context cannot be null");

		Objects.requireNonNull(
				events,
				"Events cannot be null");
		for (Event event : events) {

			log.info("{} -> {}", sourceContext, event);

			// Save the event and retrieve the generated database id.
			EventStoreJpa savedEvent = eventStoreService.append(event);

			// Publish a new immutable version of the event
			// containing its event-store id.
			eventPublisher.publishEvent(
					event.withId(savedEvent.getId()));
		}
	}
}