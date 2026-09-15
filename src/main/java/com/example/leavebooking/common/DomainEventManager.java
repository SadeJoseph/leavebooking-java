package com.example.leavebooking.common;

import com.example.leavebooking.common.events.Event;

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

			log.info(
					"{}->{}",
					sourceContext,
					event);

			// Store the event locally.
			eventStoreService.append(event);

			eventPublisher.publishEvent(event);
		}
	}
}