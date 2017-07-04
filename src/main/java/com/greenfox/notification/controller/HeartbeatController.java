package com.greenfox.notification.controller;

import com.greenfox.notification.model.interfaces.Response;
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

  @Autowired
  public HeartbeatController(ResponseService responseService, RabbitMQ rabbitMQ) {
    this.responseService = responseService;
    this.rabbitMQ = rabbitMQ;
  }

  @GetMapping("/heartbeat")
  public Response getHeartbeats(HttpServletRequest request) throws Exception {
    rabbitMQ.push(request.getRequestURI(), "heartbeat", "wohooo");
    rabbitMQ.consume("heartbeat");
    return responseService.checkForResponse();
  }
}
