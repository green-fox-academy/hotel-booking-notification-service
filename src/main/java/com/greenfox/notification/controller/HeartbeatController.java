package com.greenfox.notification.controller;

import com.greenfox.notification.model.Log;
import com.greenfox.notification.service.ResponseService;
import com.greenfox.notification.service.TimeStampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HeartbeatController {
  private final ResponseService responseService;
  private final TimeStampService timeStampService;

  @Autowired
  public HeartbeatController(ResponseService responseService, TimeStampService timeStampService) {
    this.responseService = responseService;
    this.timeStampService = timeStampService;
  }

  @GetMapping("/heartbeat")
  public Object getHeartbeats(HttpServletRequest request) {
    Log log = new Log(request.getRequestURI(), timeStampService.getISO8601CurrentDate());
    log.info("Endpoint was successfully called.");
    return responseService.checkForResponse();
  }
}
