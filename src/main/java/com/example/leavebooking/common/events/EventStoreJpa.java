package com.example.leavebooking.common.events;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity(name = "event_store")
@Table(name = "event_store")
@Getter
@Setter
@ToString
public class EventStoreJpa {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "occurred_on")
  private LocalDate occurredOn;

  @Column(name = "event_body")
  private String eventBody;

  @Column(name = "event_type")
  private String eventType;

  @Column(name = "status")
  private String status = "PENDING";

  @Column(name = "retry_count")
  private int retryCount = 0;
}
