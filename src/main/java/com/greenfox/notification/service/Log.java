package com.greenfox.notification.service;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class Log {
  private String hostName;
  private String logLevel;
  private String message;
  private String dateTime;
//  private String endPoint;
//  private static final String LOG_ENV = System.getenv("LOG_LEVEL");
  private static final List<String> levels = Arrays.asList("DEBUG", "INFO", "WARN", "ERROR");
  private TimeStampService timeStampService = new TimeStampService();

//  public Log(String hostName) {
//    this.message = message;
//    this.logLevel = logLevel;
//    this.endPoint = endPoint;
//    this.dateTime  = dateTime;
//  }

//  public Log(String logLevel, String dateTime, String hostName, String endPoint) {
//    this.logLevel = logLevel;
//    this.dateTime = dateTime;
//    this.endPoint = endPoint;
//  }
//
//  public Log(String dateTime) {
//    this.dateTime = dateTime;
//    this.logLevel = System.getenv("LOG_LEVEL");
//    this.hostName = System.getenv("HOSTNAME");
//  }

  public Log() {
    this.dateTime = timeStampService.getISO8601CurrentDate();
    this.logLevel = System.getenv("LOG_LEVEL");
    this.hostName = System.getenv("HOSTNAME");
//    this.endPoint = endPoint;
  }

  private void printLog(String requestEndpoint, String logLevel, String message) {
    if (levels.indexOf(logLevel) > 1) {
      System.err.println(logLevel + " " + dateTime + " " + hostName + " " + message + " " + "HTTP-ERROR " + requestEndpoint);
    } else {
      System.out.println(logLevel + " " + dateTime + " " + hostName + " " + message + " " + "HTTP-REQUEST " + requestEndpoint);
    }
  }

  public void debug(HttpServletRequest request, String message) {
    printLog(request.getRequestURI(),"DEBUG", message);
  }

  public void info(HttpServletRequest request, String message) {
    printLog(request.getRequestURI(),"INFO", message);
  }

  public void warn(HttpServletRequest request, String message) {
    printLog(request.getRequestURI(),"WARN", message);
  }

  public void error(HttpServletRequest request, String message) {
    printLog(request.getRequestURI(),"ERROR", message);
  }
}
