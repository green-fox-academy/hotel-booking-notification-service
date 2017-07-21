package com.greenfox.notification.controller;

import com.greenfox.notification.model.classes.booking.Booking;
import com.greenfox.notification.model.classes.booking.Bookings;
import com.greenfox.notification.repository.BookingRepository;
import com.greenfox.notification.service.TimeStampGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookingController {
  private final BookingRepository bookingRepository;
  private Bookings bookings;
  private final TimeStampGenerator timeStampGenerator;

  @Autowired
  public BookingController(BookingRepository bookingRepository, TimeStampGenerator timeStampGenerator) {
    this.bookingRepository = bookingRepository;
    this.bookings = new Bookings();
    this.timeStampGenerator = timeStampGenerator;
  }

  @GetMapping("/bookings")
  public Bookings getBookings() {
    bookings.setBookingList((List<Booking>) bookingRepository.findAll());
    return bookings;
  }

  @GetMapping("/bookingsone")
  public Bookings isReminderDueOneDay() {
    List<Booking> bookingList = bookingRepository.findAllByStartDateLessThanEqual(timeStampGenerator.getTimeStamp(1));
    bookings.setBookingList(bookingList);
    return bookings;
  }

  @GetMapping("/bookingsseven")
  public Bookings isReminderDueSevenDays() {
    List<Booking> bookingList = bookingRepository.findAllByStartDateBetween(timeStampGenerator.getTimeStamp(6),
            timeStampGenerator.getTimeStamp(7));
    bookings.setBookingList(bookingList);
    return bookings;
  }
}