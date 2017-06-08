package com.greenfox.notification.service;

import com.greenfox.notification.model.DatabaseResponse;
import com.greenfox.notification.model.OkResponse;
import com.greenfox.notification.repository.HeartbeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResponseValidator {
  private final HeartbeatRepository heartbeatRepository;

  @Autowired
  public ResponseValidator(HeartbeatRepository heartbeatRepository) {
    this.heartbeatRepository = heartbeatRepository;
  }

  public Object checkForResponse() {
    if (heartbeatRepository.count() == 0) {
      return new DatabaseResponse("ok", "error");
    } else if (heartbeatRepository.count() > 0) {
      return new DatabaseResponse("ok", "ok");
    } else {
      return new OkResponse();
    }
  }
}
