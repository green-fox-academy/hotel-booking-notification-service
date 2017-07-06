package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.booking.Booking;
import com.sendgrid.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ReminderSender {
  private Request request;
  private SendGrid sg;
  private final EmailGenerator emailGenerator;

  public ReminderSender() {
    this.request = new Request();
    this.sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
    this.emailGenerator = new EmailGenerator();
  }

  public void sendReminderMail(List<Booking> bookingList) throws IOException {
    for (Booking booking : bookingList) {
      Mail mail = emailGenerator.generateReminderEmail(booking);
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
    }
  }
}
