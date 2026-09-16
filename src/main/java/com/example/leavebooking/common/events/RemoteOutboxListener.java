package com.example.leavebooking.common.events;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AllArgsConstructor
public class RemoteOutboxListener {

  private final EventStoreService eventStoreService;
  private final RabbitTemplate rabbitTemplate;
  private final RabbitOutboxRouter rabbitOutboxRouter;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
// Spring Retry uses maxAttempts = 3 to represent the initial attempt plus two retries.
  @Retryable(retryFor = AmqpException.class, maxAttempts = 3, backoff = @Backoff(delay = 500, multiplier = 2.0))
  public void handleRemoteEvent(RemoteEvent event) {

    RabbitOutboxRouter.Destination destination;

    try {
      destination = rabbitOutboxRouter.resolve(event);

    } catch (IllegalArgumentException exception) {

      log.error(
          "Unroutable event [{}]. Check RabbitOutboxRouter configuration",
          event.getClass().getSimpleName(),
          exception);

      eventStoreService.updateStatus(
          event.getId(),
          EventStoreService.StatusOfMessageDelivery.UNROUTABLE,
          false);

      return;
    }

    rabbitTemplate.convertAndSend(
        destination.exchange(),
        destination.routingKey(),
        event);

    // If RabbitMQ send succeeds mark event as published.
    eventStoreService.updateStatus(
        event.getId(),
        EventStoreService.StatusOfMessageDelivery.PUBLISHED,
        false);
  }

  @Recover
  public void recover(
      AmqpException exception,
      RemoteEvent event) {

    log.error(
        "Failed to publish {} to RabbitMQ after retries",
        event.getId(),
        exception);

    eventStoreService.updateStatus(
        event.getId(),
        EventStoreService.StatusOfMessageDelivery.FAILED,
        true);
  }
}