package com.greenfox.notification.controller;


import com.greenfox.notification.model.classes.unsubscription.UnsubscribeInput;
import com.greenfox.notification.model.classes.unsubscription.Unsubscription;
import com.greenfox.notification.repository.UnsubscribeAttributeRepository;
import com.greenfox.notification.repository.UnsubscribeDataRepository;
import com.greenfox.notification.service.UnsubscriptionResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnsubscribeController {

  private final UnsubscribeDataRepository unsubscribeDataRepository;
  private final UnsubscribeAttributeRepository unsubscribeAttributeRepository;
  private final UnsubscriptionResponseService unsubscriptionResponseService;

  @Autowired
  public UnsubscribeController(UnsubscribeDataRepository unsubscribeDataRepository,
      UnsubscribeAttributeRepository unsubscribeAttributeRepository,
      UnsubscriptionResponseService unsubscriptionResponseService) {
    this.unsubscribeDataRepository = unsubscribeDataRepository;
    this.unsubscribeAttributeRepository = unsubscribeAttributeRepository;
    this.unsubscriptionResponseService = unsubscriptionResponseService;
  }

  @PostMapping("/unsubscriptions")
  public Unsubscription letUsersUnsubscribe(@RequestBody UnsubscribeInput unsubscribeInput) {
    return unsubscriptionResponseService.letUsersUnsubscribe(unsubscribeInput);
  }
}
