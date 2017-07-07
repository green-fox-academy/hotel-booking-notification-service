package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.booking.Booking;
import com.greenfox.notification.model.classes.booking.BookingNotification;
import com.greenfox.notification.repository.BookingNotificationRepository;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ReminderSender {
  private final BookingNotificationRepository bookingNotificationRepository;
  private Request request;
  private SendGrid sg;
  private Response response;
  private final EmailGenerator emailGenerator;

  @Autowired
  public ReminderSender(BookingNotificationRepository bookingNotificationRepository, EmailGenerator emailGenerator) {
    this.request = new Request();
    this.sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
    this.bookingNotificationRepository = bookingNotificationRepository;
    this.emailGenerator = emailGenerator;
  }

  public void sendReminderMail(List<Booking> bookingList) throws IOException {
    for (Booking booking : bookingList) {
      if (!bookingNotificationRepository.exists(booking.getEmail())) {
        Mail mail = emailGenerator.generateReminderEmail(booking);
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        response = sg.api(request);
        saveIntoRepository(booking.getEmail());
      }
    }
  }

  private void saveIntoRepository(String email) {
    BookingNotification bookingNotification = new BookingNotification(email);
    bookingNotification.setNotifiedOneDayBefore(true);
    bookingNotificationRepository.save(bookingNotification);
  }
}
