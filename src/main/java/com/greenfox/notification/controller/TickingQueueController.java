package com.greenfox.notification.controller;

import com.greenfox.notification.service.TickingQueueEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TickingQueueController {
  private final TickingQueueEventService tickingQueueEventService;

  @Autowired
  public TickingQueueController(TickingQueueEventService tickingQueueEventService) {
    this.tickingQueueEventService = tickingQueueEventService;
  }

  @GetMapping("/ticktest")
  public String eventTicking(HttpServletRequest httpServletRequest) throws Exception {
 tickingQueueEventService.consume(httpServletRequest.getRequestURI(),"consume");
 tickingQueueEventService.push(httpServletRequest.getRequestURI(),"consume","TICKED");
    return "ok ticked";
  }
}
