package com.greenfox.notification.controller;

import com.greenfox.notification.model.DatabaseResponse;
import com.greenfox.notification.model.Hearthbeat;
import com.greenfox.notification.model.OkResponse;
import com.greenfox.notification.repository.HeartbeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HearthBeatController {
  private final HeartbeatRepository heartbeatRepository;

  @Autowired
  public HearthBeatController(HeartbeatRepository heartbeatRepository) {
    this.heartbeatRepository = heartbeatRepository;
  }

  @GetMapping("/hearthbeat")
  public Object getHeartbeats() {
    List<Hearthbeat> hearthbeatList = (List<Hearthbeat>) heartbeatRepository.findAll();
    if (hearthbeatList.isEmpty()) {
      return new DatabaseResponse("ok", "error");
    } else if (!hearthbeatList.isEmpty()) {
      return new DatabaseResponse("ok", "ok");
    } else {
      return new OkResponse();
    }
  }
}
