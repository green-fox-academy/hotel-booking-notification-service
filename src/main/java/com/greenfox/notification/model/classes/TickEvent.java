package com.greenfox.notification.model.classes;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TickEvent {
  private int tickCounter;
  private Object message;

  public TickEvent(Object message) {
    this.tickCounter = tickCounter;
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