package com.greenfox.notification.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by zsuzsanna.padar on 2017. 06. 15..
 */
public class TimeStampUtil {
  public static String getISO8601StringForCurrentDate() {
    Date now = new Date();
    return getISO8601StringForDate(now);
  }

  private static String getISO8601StringForDate(Date date) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
    return dateFormat.format(date);
  }
}
