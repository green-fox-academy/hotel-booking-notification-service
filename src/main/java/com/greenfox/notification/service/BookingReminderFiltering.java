package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.booking.Booking;
import com.greenfox.notification.model.classes.booking.Bookings;

import java.util.ArrayList;
import java.util.List;

public class BookingReminderFiltering {
  private TimeStampGenerator timeStampGenerator;

  public BookingReminderFiltering() {
    this.timeStampGenerator = new TimeStampGenerator();
  }

  public List<Booking> findBookingsWithinOneDay(Bookings bookings) {
    List<Booking> bookingList = bookings.getBookingList();
    List<Booking> returnList = new ArrayList<>();
    for (Booking booking : bookingList) {
      if (booking.getStartDate().before(timeStampGenerator.getTimeStamp(1)) &&
              !booking.getStartDate().before(timeStampGenerator.getTimeStampNow())) {
        returnList.add(booking);
      }
    }
    return returnList;
  }
}
