package com.example.leavebooking.common;

import com.example.leavebooking.common.events.Event;

import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot<T> extends Entity<T> {

  public List<Event> domainEvents = new ArrayList<>();

  public AggregateRoot(Identity<T> id) {
    super(id);
  }

  protected void addDomainEvent(Event event) {
    domainEvents.add(event);
  }

  protected void removeDomainEvent(Event event) {
    domainEvents.remove(event);
  }

  public List<Event> listOfDomainEvents() {
    return domainEvents;
  }

  public void clearDomainEvents() {
    domainEvents.clear();
  }

  public boolean domainEventsExist() {
    return !domainEvents.isEmpty();
  }
}