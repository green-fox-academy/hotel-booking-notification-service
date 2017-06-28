package com.greenfox.notification.service;

import com.greenfox.notification.repository.UnsubscribeDataRepository;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnsubscriptionResponseService {
  private final UnsubscribeDataRepository unsubcriptionRepository;
  private AtomicLong atomicLong;


  @Autowired
  public UnsubscriptionResponseService(UnsubscribeDataRepository unsubcriptionRepository) {
    this.unsubcriptionRepository = unsubcriptionRepository;
    this.atomicLong = new AtomicLong(1);
  }

}
