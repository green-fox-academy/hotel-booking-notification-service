package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.booking.Booking;
import com.greenfox.notification.model.classes.booking.Bookings;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
@Getter
@Setter
public class BookingReminderFiltering {
  private static List<Booking> filteredList;

  public BookingReminderFiltering() {
  }

  public static List<Booking> findBookings(Bookings bookings, Predicate<Booking> startDate) {
    List<Booking> bookingList = bookings.getBookingList();
    filteredList = new ArrayList<>();
    for (Booking booking : bookingList) {
      if (startDate.test(booking)) {
        filteredList.add(booking);
      }
    }
    return filteredList;
  }
}
