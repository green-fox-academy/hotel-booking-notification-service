package com.greenfox.notification.model.classes;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Event {
  private String time;
  private String hostname;
  private Object message;

  public Event(Object message) {
    this.time = String.valueOf(LocalDate.now());
    this.hostname = System.getenv("HOSTNAME");
    this.message = message;
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
