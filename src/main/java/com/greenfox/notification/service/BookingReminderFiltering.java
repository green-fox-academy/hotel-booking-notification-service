package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.booking.Booking;
import com.greenfox.notification.model.classes.booking.Bookings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingReminderFiltering {
  private final TimeStampGenerator timeStampGenerator;

  @Autowired
  public BookingReminderFiltering(TimeStampGenerator timeStampGenerator) {
    this.timeStampGenerator = timeStampGenerator;
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
