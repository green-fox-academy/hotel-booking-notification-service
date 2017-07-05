package com.greenfox.notification.service;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class TimeStampGenerator {

  private long dayToMilliSeconds(int days) {
    return (long) (days * 24 * 60 * 60 * 1000);
  }

  public Timestamp getTimeStamp(int days) {
    Instant instant = Instant.now();
    Timestamp timeStamp = java.sql.Timestamp.from(instant);
    long milliSeconds = dayToMilliSeconds(days);
    return new Timestamp(timeStamp.getTime() + milliSeconds);
  }
}
