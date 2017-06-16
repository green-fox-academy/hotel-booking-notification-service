package com.greenfox.notification.controller;

import com.greenfox.notification.model.Log;
import com.greenfox.notification.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HeartbeatController {
  private final ResponseService responseService;

  @Autowired
  public HeartbeatController(ResponseService responseService) {
    this.responseService = responseService;
  }

  @GetMapping("/heartbeat")
  public Object getHeartbeats(HttpServletRequest request) {
    Log log = new Log(request.getRequestURI());
    log.info("Endpoint was successfully called.");
    log.showLog();
    return responseService.checkForResponse();
  }
}
