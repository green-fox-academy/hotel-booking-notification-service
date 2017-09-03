package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.booking.Booking;
import com.greenfox.notification.model.classes.booking.Bookings;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Getter
@Setter
public class EmailTimerService {
  private RestTemplate restTemplate;
  private int interval;
  private final Log log;
  private final TickingQueueEventService tickingQueueEventService;
  private final TimeStampGenerator timeStampGenerator;
  private final ReminderSender reminderSender;
  private static final String URL = "https://" + System.getenv("HOSTNAME") + "/bookings";

  @Autowired
  public EmailTimerService(ReminderSender reminderSender, TickingQueueEventService tickingQueueEventService,
                           TimeStampGenerator timeStampGenerator, Log log) {
    this.restTemplate = new RestTemplate();
    this.reminderSender = reminderSender;
    this.log = log;
    this.tickingQueueEventService = tickingQueueEventService;
    this.timeStampGenerator = timeStampGenerator;
    this.interval = 15;
  }

  private Runnable createTask() {
    Runnable task = () -> {
      Bookings bookings = restTemplate.getForObject(URL, Bookings.class);
      List<Booking> bookingsWithinOneDay = BookingReminderFiltering.findBookings(bookings, booking ->
          booking.getStartDate().before(timeStampGenerator.getTimeStamp(1)) &&
          !booking.getStartDate().before(timeStampGenerator.getTimeStampNow()));
      List<Booking> bookingsWithinSevenDays = BookingReminderFiltering.findBookings(bookings, booking ->
          booking.getStartDate().before(timeStampGenerator.getTimeStamp(7)) &&
          booking.getStartDate().after(timeStampGenerator.getTimeStamp(6)) &&
          !booking.getStartDate().before(timeStampGenerator.getTimeStampNow()));
      List<Booking> bookingsWithinFourteenDays = BookingReminderFiltering.findBookings(bookings, booking ->
          booking.getStartDate().before(timeStampGenerator.getTimeStamp(14)) &&
          booking.getStartDate().after(timeStampGenerator.getTimeStamp(13)) &&
          !booking.getStartDate().before(timeStampGenerator.getTimeStampNow()));
      try {
        reminderSender.sendReminderFourteenDaysBefore(bookingsWithinFourteenDays);
        reminderSender.sendReminderSevenDaysBefore(bookingsWithinSevenDays);
        reminderSender.sendReminderOneDayBefore(bookingsWithinOneDay);
      } catch (IOException e) {
        log.error("task creating", e.getMessage());
      }
    };
    return task;
  }

  public void sendReminderEmail() {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(createTask(), 0, interval, TimeUnit.MINUTES);
    tickingQueueEventService.push("No HTTP Request", "ticking queue", "Pushed event");
    try {
      tickingQueueEventService.consume("No HTTP Request", "ticking queue");
    } catch (Exception e) {
      log.error("ticking queue", e.getMessage());
    }
  }
}



