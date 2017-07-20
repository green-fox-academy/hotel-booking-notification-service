package com.greenfox.notification.controller;


import com.greenfox.notification.model.classes.unsubscription.UnsubscribeInput;
import com.greenfox.notification.model.interfaces.Response;
import com.greenfox.notification.service.UnsubscriptionResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnsubscribeController {
  private final UnsubscriptionResponseService unsubscriptionResponseService;

  @Autowired
  public UnsubscribeController(UnsubscriptionResponseService unsubscriptionResponseService) {
    this.unsubscriptionResponseService = unsubscriptionResponseService;
  }

  @PostMapping("/unsubscriptions")
  public Response letUsersUnsubscribe(@RequestBody UnsubscribeInput unsubscribeInput) {
    return unsubscriptionResponseService.letUsersUnsubscribe(unsubscribeInput);
  }
}
