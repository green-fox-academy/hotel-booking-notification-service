package com.greenfox.notification.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;

import com.sendgrid.Mail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {
  private String time;
  private String hostname;
  private String message;
  private Mail mail;

  public Event(String message) {
    this.time = String.valueOf(LocalDate.now());
    this.hostname = System.getenv("HOSTNAME");
    this.message = message;
  }

  public Event(Mail mail) {
    this.mail = mail;
    this.time = String.valueOf(LocalDate.now());
    this.hostname = System.getenv("HOSTNAME");
  }

  public static String asJsonString(final Object obj) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
