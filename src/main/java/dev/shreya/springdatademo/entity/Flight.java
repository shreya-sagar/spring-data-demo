package dev.shreya.springdatademo.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Flight {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String origin;
  private String destination;
  private LocalDateTime scheduledAt;

  public Flight() {
  }

  public Flight(String origin, String destination, LocalDateTime scheduledAt) {
    this.origin = origin;
    this.destination = destination;
    this.scheduledAt = scheduledAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public LocalDateTime getScheduledAt() {
    return scheduledAt;
  }

  public void setScheduledAt(LocalDateTime scheduledAt) {
    this.scheduledAt = scheduledAt;
  }
}
