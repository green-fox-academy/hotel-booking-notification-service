package com.greenfox.notification.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Log {
  private static String HOSTNAME = "hotel-booking-notification-service.herokuapp.com";
  private String logLevel;
  private String message;
  private String dateTime;
  private String endPoint;
  private static final String ENV = System.getenv("LOG_LEVEL");
  private static final List<String> levels = Arrays.asList("DEBUG", "INFO", "WARN", "ERROR");

  public Log(String hostName) {
    this.message = message;
    this.logLevel = logLevel;
    this.endPoint = endPoint;
    this.dateTime  = dateTime;
  }

  public Log(String logLevel, String dateTime, String hostName, String endPoint) {
    this.logLevel = logLevel;
    this.dateTime = dateTime;
    this.endPoint = endPoint;
  }

  public Log(String endPoint, String dateTime) {
    this.dateTime = dateTime;
    this.endPoint = endPoint;
  }

  private void printLog(String logLevel, String message) {
    if (levels.indexOf(logLevel) > 1) {
      System.err.println(logLevel + " " + dateTime + " " + HOSTNAME + " " + message + " " + "HTTP-ERROR " + endPoint);
    } else {
      System.out.println(logLevel + " " + dateTime + " " + HOSTNAME + " " + message + " " + "HTTP-REQUEST " + endPoint);
    }
  }

  public void debug(String message) {
    printLog("DEBUG", message);
  }

  public void info(String message) {
    printLog("INFO", message);
  }

  public void warn(String message) {
    printLog("WARN", message);
  }

  public void error(String message) {
    printLog("ERROR", message);
  }
}
