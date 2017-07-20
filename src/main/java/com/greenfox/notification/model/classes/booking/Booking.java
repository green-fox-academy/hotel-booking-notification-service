package com.greenfox.notification.model.classes.booking;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "booking")
public class Booking {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private int guest;
  private Timestamp startDate;
  private Timestamp endDate;
  private Timestamp createdAt;
  private String description;
  private String email;
  private String contactName;

  public Booking(Long id, int guest, Timestamp startDate, Timestamp endDate,
                 Timestamp createdAt, String contactName, String email) {
    this.id = id;
    this.guest = guest;
    this.startDate = startDate;
    this.endDate = endDate;
    this.createdAt = createdAt;
    this.contactName = contactName;
    this.email = email;
  }

  public Booking() {
  }

  @Override
  public String toString() {
    return "Booking{" +
            "id=" + id +
            ", guest=" + guest +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", createdAt=" + createdAt +
            ", description='" + description + '\'' +
            ", email='" + email + '\'' +
            ", contactName='" + contactName + '\'' +
            '}';
  }
}
