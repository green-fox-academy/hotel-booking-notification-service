package com.greenfox.notification.model.classes.booking;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Booking {
  @Id
  @GeneratedValue( strategy = GenerationType.AUTO)
  private Long id;
  private int guest;
  private Timestamp startDate;
  private Timestamp endDate;
  private Timestamp createdAt;
  private String description;

  public Booking(Long id, int guest, Timestamp startDate, Timestamp endDate,
      Timestamp createdAt) {
    this.id = id;
    this.guest = guest;
    this.startDate = startDate;
    this.endDate = endDate;
    this.createdAt = createdAt;
  }
}
