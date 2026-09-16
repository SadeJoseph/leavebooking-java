package com.example.leavebooking.common.events;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@ConfigurationProperties(prefix = "rabbitmq.outbox")
@Getter
public class RabbitOutboxRouter {

  public record Destination(
      String exchange,
      String routingKey) {
  }

  private final Map<String, Destination> bindings = new HashMap<>();

  public Destination resolve(Event event) {

    String className = event.getClass().getName();

    Destination destination = bindings.get(className);

    if (destination == null) {
      throw new IllegalArgumentException(
          "No RabbitMQ destination configured for "
              + className);
    }

    return destination;
  }
}