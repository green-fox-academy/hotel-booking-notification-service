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
  private List<Booking> filteredList;

  @Autowired
  public BookingReminderFiltering(TimeStampGenerator timeStampGenerator) {
    this.timeStampGenerator = timeStampGenerator;
    this.filteredList = new ArrayList<>();
  }

  public List<Booking> findBookingsWithinOneDay(Bookings bookings) {
    List<Booking> bookingList = bookings.getBookingList();
    for (Booking booking : bookingList) {
      if (booking.getStartDate().before(timeStampGenerator.getTimeStamp(1)) &&
              !booking.getStartDate().before(timeStampGenerator.getTimeStampNow())) {
        filteredList.add(booking);
      }
    }
    return filteredList;
  }

  public List<Booking> findBookingsWithinSevenDay(Bookings bookings) {
    List<Booking> bookingList = bookings.getBookingList();
    for (Booking booking : bookingList) {
      if (booking.getStartDate().before(timeStampGenerator.getTimeStamp(6)) &&
          booking.getStartDate().after(timeStampGenerator.getTimeStamp(7)) &&
          !booking.getStartDate().before(timeStampGenerator.getTimeStampNow())) {
        filteredList.add(booking);
      }
    }
    return filteredList;
  }
}
