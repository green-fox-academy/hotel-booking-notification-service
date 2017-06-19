package com.greenfox.notification.service;

import com.greenfox.notification.model.DatabaseResponse;
import com.greenfox.notification.model.OkResponse;
import com.greenfox.notification.repository.HeartbeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {
  private final HeartbeatRepository heartbeatRepository;
  private RabbitMQ rabbitMQ;

  @Autowired
  public ResponseService(HeartbeatRepository heartbeatRepository, RabbitMQ rabbitMQ) {
    this.heartbeatRepository = heartbeatRepository;
    this.rabbitMQ = rabbitMQ;
  }

  public Object checkForResponse() {
    if (heartbeatRepository.count() == 0 && rabbitMQ.getQueueMessageSize() == 0) {
      return new DatabaseResponse("ok", "error", "ok");
    } else if (heartbeatRepository.count() > 0 && (rabbitMQ.getQueueMessageSize() > 0 || !rabbitMQ.getConnection().isOpen())) {
      return new DatabaseResponse("ok", "ok", "error");
    } else {
      return new OkResponse();
    }
  }
}
