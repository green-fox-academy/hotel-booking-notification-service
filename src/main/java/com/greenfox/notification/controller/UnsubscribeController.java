package com.greenfox.notification.controller;


import com.greenfox.notification.model.classes.unsubscription.Error;
import com.greenfox.notification.model.classes.unsubscription.Errors;
import com.greenfox.notification.model.classes.unsubscription.UnsubscribeAttribute;
import com.greenfox.notification.model.classes.unsubscription.UnsubscribeInput;
import com.greenfox.notification.repository.UnsubscribeAttributeRepository;
import com.greenfox.notification.repository.UnsubscribeDataRepository;
import com.greenfox.notification.service.UnsubscriptionResponseService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
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
  public Object letUsersUnsubscribe(@Valid UnsubscribeAttribute unsubscribeAttribute,
      BindingResult bindingResult, @RequestBody UnsubscribeInput unsubscribeInput) {
    if (bindingResult.hasErrors()) {
      return new Errors(new Error());
    }
    return unsubscriptionResponseService.letUsersUnsubscribe(unsubscribeInput);
  }
}
