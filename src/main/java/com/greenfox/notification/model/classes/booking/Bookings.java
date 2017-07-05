package com.greenfox.notification.model.classes.booking;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Bookings {
  private List<Booking> bookingList;

  public Bookings(){
    this.bookingList = new ArrayList<>();
  }
}
