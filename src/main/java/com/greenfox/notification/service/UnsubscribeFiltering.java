package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.heartbeat.Data;
import com.greenfox.notification.repository.UnsubscribeDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnsubscribeFiltering {
  private final UnsubscribeDataRepository unsubscribeDataRepository;

  @Autowired
  public UnsubscribeFiltering(UnsubscribeDataRepository unsubscribeDataRepository) {
    this.unsubscribeDataRepository = unsubscribeDataRepository;
  }

  public boolean checkIfUserUnsubscribed(Data data) {
    if (unsubscribeDataRepository.findByAttributes_Email(data.getAttributes().getEmail()) == null) {
      return false;
    } else {
      return true;
    }
  }
}