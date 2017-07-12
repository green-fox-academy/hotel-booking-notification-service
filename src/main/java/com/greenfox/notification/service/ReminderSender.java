package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.booking.Booking;
import com.greenfox.notification.model.classes.booking.BookingNotification;
import com.greenfox.notification.repository.BookingNotificationRepository;
import com.sendgrid.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Getter
@Setter
public class ReminderSender {
  private final BookingNotificationRepository bookingNotificationRepository;
  private Request request;
  private SendGrid sg;
  private Response response;
  private final EmailGenerator emailGenerator;
  private final Log log;
  private final TimeStampGenerator timeStampGenerator;

  @Autowired
  public ReminderSender(BookingNotificationRepository bookingNotificationRepository, EmailGenerator emailGenerator,
                        Log log, TimeStampGenerator timeStampGenerator) {
    this.request = new Request();
    this.sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
    this.bookingNotificationRepository = bookingNotificationRepository;
    this.emailGenerator = emailGenerator;
    this.log = log;
    this.timeStampGenerator = timeStampGenerator;
  }

  public void sendReminderFourteenDaysBefore(List<Booking> bookingList) throws IOException {
    for (Booking booking : bookingList) {
      if (!bookingNotificationRepository.exists(booking.getEmail())) {
        Mail mail = emailGenerator.generateReminderEmail(booking);
        response = sg.api(setRequestConfiguration(mail));
        saveIntoRepository(booking.getEmail());
      }
    }
  }

  public void sendReminderSevenDaysBefore(List<Booking> bookingList) throws IOException {
    for (Booking booking : bookingList) {
      if (!bookingNotificationRepository.findOne(booking.getEmail()).isNotifiedSevenDaysBefore()) {
        Mail mail = emailGenerator.generateReminderEmail(booking);
        response = sg.api(setRequestConfiguration(mail));
        BookingNotification bookingNotification = bookingNotificationRepository.findOne(booking.getEmail());
        bookingNotification.setNotifiedSevenDaysBefore(true);
        bookingNotificationRepository.save(bookingNotification);
      }
    }
  }

  public void sendReminderOneDayBefore(List<Booking> bookingList) throws IOException {
    for (Booking booking : bookingList) {
      if (!bookingNotificationRepository.findOne(booking.getEmail()).isNotifiedOneDayBefore()) {
        Mail mail = emailGenerator.generateReminderEmail(booking);
        response = sg.api(setRequestConfiguration(mail));
        BookingNotification bookingNotification = bookingNotificationRepository.findOne(booking.getEmail());
        bookingNotification.setNotifiedOneDayBefore(true);
        bookingNotificationRepository.save(bookingNotification);
      }
    }
  }

  private void saveIntoRepository(String email) {
    BookingNotification bookingNotification = new BookingNotification(email);
    bookingNotification.setNotifiedFourteenDaysBefore(true);
    bookingNotificationRepository.save(bookingNotification);
  }

  private Request setRequestConfiguration(Mail mail) throws IOException {
    request.setMethod(Method.POST);
    request.setEndpoint("mail/send");
    request.setBody(mail.build());
    return request;
  }
}
