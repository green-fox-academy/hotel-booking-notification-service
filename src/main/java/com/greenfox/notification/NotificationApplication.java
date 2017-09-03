package com.greenfox.notification;

import com.greenfox.notification.service.EmailTimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableAspectJAutoProxy
public class NotificationApplication {
  private static EmailTimerService staticEmailTimerService;
  private EmailTimerService emailTimerService;

  @Autowired
  public NotificationApplication(EmailTimerService emailTimerService) {
    this.emailTimerService = emailTimerService;
  }

  @PostConstruct
  public void init() {
    NotificationApplication.staticEmailTimerService = emailTimerService;
  }

  public static void main(String[] args) {
    SpringApplication.run(NotificationApplication.class, args);
    staticEmailTimerService.sendReminderEmail();
  }
}
