package com.greenfox.notification.controller;

import com.greenfox.notification.model.interfaces.Response;
import com.greenfox.notification.service.Log;
import com.greenfox.notification.service.RabbitMQ;
import com.greenfox.notification.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HeartbeatController {
  private final ResponseService responseService;
  private final RabbitMQ rabbitMQ;
  private final Log log;

  @Autowired
  public HeartbeatController(ResponseService responseService, RabbitMQ rabbitMQ, Log log) {
    this.responseService = responseService;
    this.rabbitMQ = rabbitMQ;
    this.log = log;
  }

  @GetMapping("/heartbeat")
  public Response getHeartbeats(HttpServletRequest request) throws Exception {
    log.info(request, "Endpoint was successfully called.");
    rabbitMQ.push(request, "heartbeat", "wohooo");
    rabbitMQ.consume(request, "heartbeat");
    return responseService.checkForResponse();
  }
}
