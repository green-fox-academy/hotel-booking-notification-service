package com.greenfox.notification;

import com.greenfox.notification.model.classes.booking.Booking;
import com.greenfox.notification.model.classes.booking.Bookings;
import com.greenfox.notification.service.BookingReminderFiltering;
import com.greenfox.notification.service.Log;
import com.greenfox.notification.service.TickingQueueEventService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

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

  public static void main(String[] args) throws URISyntaxException, IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException {
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
        tickingQueueEventService.push("No HTTP Request", "consume", "Pushed event");
        try {
          tickingQueueEventService.consume("No HTTP Request", "consume");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }, 0, 1000 * 1 * MINUTES);
  }
}

