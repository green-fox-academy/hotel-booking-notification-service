package com.greenfox.notification.controller;

import com.greenfox.notification.model.classes.Event;
import com.greenfox.notification.service.TickingQueueEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TickingQueueController {
  private final TickingQueueEventService tickingQueueEventService;
  private Event event;

  @Autowired
  public TickingQueueController(TickingQueueEventService tickingQueueEventService) {
    this.tickingQueueEventService = tickingQueueEventService;
    this.event = event;
  }

  @GetMapping("/ticktest")
  public int eventTicking(HttpServletRequest httpServletRequest) throws Exception {
 tickingQueueEventService.consumeEvent(httpServletRequest.getRequestURI());
 tickingQueueEventService.pushEvent(httpServletRequest.getRequestURI(),event);
 event.setTickCounter(event.getTickCounter() + 1);
    return event.getTickCounter();
  }
}
