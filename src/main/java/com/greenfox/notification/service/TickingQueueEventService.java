package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.Event;
import com.sendgrid.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TickingQueueEventService {
  private final Log log;
  private Request request;
  private final RabbitMQ rabbitMQ;

  @Autowired
  public TickingQueueEventService(Log log, RabbitMQ rabbitMQ) {
    this.log = log;
    this.request = new Request();
    this.rabbitMQ = rabbitMQ;

  }

  public void pushEvent(String servletRequest, Event event) {
    rabbitMQ.push(servletRequest, "consume", event);
  }

  public void consumeEvent(String servletRequest) {
    try {
      rabbitMQ.consume(servletRequest, "consume");
    } catch (Exception e) {
      log.error(servletRequest, e.getMessage());
    }
  }
}
