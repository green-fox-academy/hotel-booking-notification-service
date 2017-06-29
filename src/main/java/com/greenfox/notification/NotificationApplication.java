package com.greenfox.notification;

import com.greenfox.notification.service.Log;
import com.greenfox.notification.service.TickingQueueEventService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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
    int MINUTES = 10;
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        tickingQueueEventService.push("No HTTP Request","consume","Pushed event");
        try {
          tickingQueueEventService.consume("No HTTP Request","consume");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }, 0, 1000 * 60 * MINUTES);
    // 1000 milliseconds in a second * 60 per minute * the MINUTES variable.
  }
}

