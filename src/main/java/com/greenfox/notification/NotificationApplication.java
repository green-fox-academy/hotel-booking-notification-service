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
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
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
@EnableSpringConfigured
public class NotificationApplication {
  private static ReminderSender staticReminderSender;
  @Autowired
  private ReminderSender reminderSender;

  @PostConstruct
  public void init(){
    NotificationApplication.staticReminderSender = reminderSender;
  }

  public static void main(String[] args) throws URISyntaxException, IOException, TimeoutException,
                                                NoSuchAlgorithmException, KeyManagementException {
    SpringApplication.run(NotificationApplication.class, args);
    Log log = new Log();
    TickingQueueEventService tickingQueueEventService = new TickingQueueEventService(log);
    int MINUTES = 15;
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      private BookingReminderFiltering bookingReminderFiltering = new BookingReminderFiltering();
      private RestTemplate restTemplate = new RestTemplate();
      @Override
      public void run() {
        Bookings bookings = restTemplate.getForObject("http://localhost:8080/bookings", Bookings.class);
        List<Booking> bookingsWithinOneDay = bookingReminderFiltering.findBookingsWithinOneDay(bookings);
        try {
          staticReminderSender.sendReminderMail(bookingsWithinOneDay);
        } catch (IOException e) {
          log.error("reminder send in main() ", e.getMessage());
        }
        tickingQueueEventService.push("No HTTP Request", "ticking queue", "Pushed event");
        try {
          tickingQueueEventService.consume("No HTTP Request", "ticking queue");
        } catch (Exception e) {
          log.error("ticking que", e.getMessage());
        }
      }
    }, 0, 1000 * 1 * MINUTES);
  }
}

