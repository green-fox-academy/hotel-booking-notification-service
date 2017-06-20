package com.greenfox.notification.controller;

import com.greenfox.notification.model.Log;
import com.greenfox.notification.service.RabbitMQ;
import com.greenfox.notification.service.ResponseService;
import com.greenfox.notification.service.TimeStampService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeartbeatController {
  private final ResponseService responseService;
  private final TimeStampService timeStampService;
  private final RabbitMQ rabbitMQ;

  @Autowired
  public HeartbeatController(ResponseService responseService, TimeStampService timeStampService, RabbitMQ rabbitMQ) {
    this.responseService = responseService;
    this.timeStampService = timeStampService;
    this.rabbitMQ = rabbitMQ;
  }

  @GetMapping("/heartbeat")
  public Object getHeartbeats(HttpServletRequest request) throws Exception {
    Log log = new Log(request.getRequestURI(), timeStampService.getISO8601CurrentDate());
    log.info("Endpoint was successfully called.");
    rabbitMQ.push("heartbeat", "wohooo");
    rabbitMQ.consume("heartbeat");
    return responseService.checkForResponse();
  }
}
