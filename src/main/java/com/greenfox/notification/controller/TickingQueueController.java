package com.greenfox.notification.controller;

import com.greenfox.notification.model.classes.Data;
import com.greenfox.notification.service.RabbitMQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TickingQueueController {

  private RabbitMQ rabbitMQ;

  @Autowired
  public TickingQueueController(RabbitMQ rabbitMQ){
    this.rabbitMQ = rabbitMQ;
  }

  @PostMapping("/tickqueue")
  public Data registration(@RequestBody Data data, HttpServletRequest request, Object queue, Object message) throws Exception {
    rabbitMQ.consume(request.getRequestURI(),"consumee");
    rabbitMQ.push(request.getRequestURI(),"consume","pushed");
    return data;
  }

}
