package com.greenfox.notification.controller;

import com.greenfox.notification.model.Log;
import com.greenfox.notification.service.ResponseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HeartbeatController {
  private final ResponseValidator responseValidator;

  @Autowired
  public HeartbeatController(ResponseValidator responseValidator) {
    this.responseValidator = responseValidator;
  }

  @GetMapping("/heartbeat")
  public Object getHeartbeats(HttpServletRequest request) {
    Log log = new Log(request.getRequestURI());
    log.info("Endpoint was successfully called.");
    log.showLog();
    return responseValidator.checkForResponse();
  }
}
