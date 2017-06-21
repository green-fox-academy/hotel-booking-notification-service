package com.greenfox.notification.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {
  private String time;
  private String hostname;
  private String message;

  public Event(String message) {
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
