package com.greenfox.notification.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Log {
  private String hostname;
  private String logLevel;
  private String message;
  private static final String ENV = System.getenv("LOG_LEVEL");

  public Log(String hostname) {
    this.hostname = hostname;
    this.logLevel = logLevel;
    this.message = message;
  }

  public void showLog() {
    if (logLevel.equals("ERROR") || logLevel.equals("WARN")) {
      System.err.println(this);
    } else if (!(ENV.equals("ERROR") && ENV.equals("WARN"))) {
      System.out.println(this);
    }
  }

  public void setLogLevelDebug(String message) {
    setLogLevel("DEBUG");
    setMessage(message);
  }

  public void setLogLevelInfo(String message) {
    setLogLevel(ENV);
    setMessage(message);
  }

  public void setLogLevelWarn(String message) {
    setLogLevel("WARN");
    setMessage(message);
  }

  public void setLogLevelError(String message) {
    setLogLevel("ERROR");
    setMessage(message);
  }

  @Override
  public String toString() {
    return logLevel + " " + hostname + " " + message;
  }
}
