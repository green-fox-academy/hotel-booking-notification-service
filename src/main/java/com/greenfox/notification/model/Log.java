package com.greenfox.notification.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Log {
  private String hostname;
  private String logLevel;
  private String message;
  private String dateTime;
  private static final String ENV = System.getenv("LOG_LEVEL");

  public Log(String hostname) {
    this.hostname = hostname;
    this.dateTime  = TimeStampUtil.getISO8601StringForCurrentDate();
  }

  public void showLog() {
    if (logLevel.equals("ERROR") || logLevel.equals("WARN")) {
      System.err.println(this);
    } else if (!(ENV.equals("ERROR") && ENV.equals("WARN"))) {
      System.out.println(this);
    }
  }

  public void debug(String message) {
    setLogLevel("DEBUG");
    setMessage(message);
  }

  public void info(String message) {
    setLogLevel(ENV);
    setMessage(message);
  }

  public void warn(String message) {
    setLogLevel("WARN");
    setMessage(message);
  }

  public void error(String message) {
    setLogLevel("ERROR");
    setMessage(message);
  }

  @Override
  public String toString() {
    return logLevel + " " + dateTime + " " + hostname + " " + message;
  }
}
