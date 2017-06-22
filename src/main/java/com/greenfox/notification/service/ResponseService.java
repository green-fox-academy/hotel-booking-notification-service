package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.DatabaseResponse;
import com.greenfox.notification.model.classes.OkResponse;
import com.greenfox.notification.repository.HeartbeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ResponseService {
  private final HeartbeatRepository heartbeatRepository;
  private RabbitMQ rabbitMQ;
  private String queueName = System.getenv("QUEUE_NAME");

  @Autowired
  public ResponseService(HeartbeatRepository heartbeatRepository, RabbitMQ rabbitMQ) {
    this.heartbeatRepository = heartbeatRepository;
    this.rabbitMQ = rabbitMQ;
  }

  public Object checkForResponse() throws IOException {
    if (heartbeatRepository.count() == 0 && rabbitMQ.isQueueEmpty(queueName)) {
      return new DatabaseResponse("ok", "error", "ok");
    } else if (heartbeatRepository.count() > 0 && (!rabbitMQ.isQueueEmpty(queueName) || !rabbitMQ.getConnection().isOpen())) {
      return new DatabaseResponse("ok", "ok", "error");
    } else if (heartbeatRepository.count() > 0 && (rabbitMQ.isQueueEmpty(queueName) && rabbitMQ.getConnection().isOpen())) {
      return new DatabaseResponse("ok", "ok", "ok");
    } else {
      return new OkResponse();
    }
  }
}