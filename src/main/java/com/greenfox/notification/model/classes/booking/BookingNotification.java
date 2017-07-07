package com.greenfox.notification.model.classes.booking;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "notified_bookings")
public class BookingNotification {
  @Id
  private String email;
  private boolean notifiedOneDayBefore;
  private boolean notifiedSevenDaysBefore;
  private boolean notifiedFourteenDaysBefore;

  public BookingNotification(String email) {
    this.email = email;
    this.notifiedOneDayBefore = false;
    this.notifiedSevenDaysBefore = false;
    this.notifiedFourteenDaysBefore = false;
  }
}
