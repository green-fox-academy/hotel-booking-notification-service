package com.greenfox.notification.controller;

import com.greenfox.notification.service.ResponseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HearthBeatController {
  private final ResponseValidator responseValidator;

  @Autowired
  public HearthBeatController(ResponseValidator responseValidator) {
    this.responseValidator = responseValidator;
  }

  @GetMapping("/hearthbeat")
  public Object getHeartbeats() {
    return responseValidator.checkForResponse();
  }
}
