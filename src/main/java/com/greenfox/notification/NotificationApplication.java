package com.greenfox.notification;

import com.greenfox.notification.model.classes.booking.Booking;
import com.greenfox.notification.model.classes.booking.Bookings;
import com.greenfox.notification.service.BookingReminderFiltering;
import com.greenfox.notification.service.Log;
import com.greenfox.notification.service.ReminderSender;
import com.greenfox.notification.service.TickingQueueEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
@EnableAspectJAutoProxy
public class NotificationApplication {
  private static ReminderSender staticReminderSender;
  private static BookingReminderFiltering staticBookingReminderFiltering;
  private static RestTemplate restTemplate;
  private static Log staticLog;
  private static Timer timer;
  private static TickingQueueEventService staticTickingQueueEventService;
  private final ReminderSender reminderSender;
  private final BookingReminderFiltering bookingReminderFiltering;
  private final Log log;
  private final TickingQueueEventService tickingQueueEventService;
  private static int minutes;

  @Autowired
  public NotificationApplication(ReminderSender reminderSender, BookingReminderFiltering bookingReminderFiltering,
                                 Log log, TickingQueueEventService tickingQueueEventService) {
    restTemplate = new RestTemplate();
    timer = new Timer();
    this.reminderSender = reminderSender;
    this.bookingReminderFiltering = bookingReminderFiltering;
    this.log = log;
    this.tickingQueueEventService = tickingQueueEventService;
    minutes = 15;
  }

  @PostConstruct
  public void init() {
    NotificationApplication.staticReminderSender = reminderSender;
    NotificationApplication.staticBookingReminderFiltering = bookingReminderFiltering;
    NotificationApplication.staticLog = log;
    NotificationApplication.staticTickingQueueEventService = tickingQueueEventService;
  }

  public static void main(String[] args) throws URISyntaxException, IOException, TimeoutException,
                                                NoSuchAlgorithmException, KeyManagementException {
    SpringApplication.run(NotificationApplication.class, args);
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        Bookings bookings = restTemplate.getForObject("https://" + System.getenv("HOSTNAME") + "/bookings", Bookings.class);
        List<Booking> bookingsWithinOneDay = staticBookingReminderFiltering.findBookingsWithinOneDay(bookings);
        List<Booking> bookingsWithinSevenDays = staticBookingReminderFiltering.findBookingsWithinSevenDays(bookings);
        List<Booking> bookingsWithinFourteenDays = staticBookingReminderFiltering.findBookingsWithinFourteenDays(bookings);
        try {
          staticReminderSender.sendReminderFourteenDaysBefore(bookingsWithinFourteenDays);
          staticReminderSender.sendReminderSevenDaysBefore(bookingsWithinSevenDays);
          staticReminderSender.sendReminderOneDayBefore(bookingsWithinOneDay);
        } catch (IOException e) {
          staticLog.error("reminder send in main() ", e.getMessage());
        }
        staticTickingQueueEventService.push("No HTTP Request", "ticking queue", "Pushed event");
        try {
          staticTickingQueueEventService.consume("No HTTP Request", "ticking queue");
        } catch (Exception e) {
          staticLog.error("ticking queue", e.getMessage());
        }
      }
    }, 0, 1000 * 60 * minutes);
  }
}