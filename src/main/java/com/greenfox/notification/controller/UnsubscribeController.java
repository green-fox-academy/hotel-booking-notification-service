package com.greenfox.notification.controller;


import com.greenfox.notification.model.classes.Link;
import com.greenfox.notification.model.classes.input.UnsubscribeAttribute;
import com.greenfox.notification.model.classes.input.UnsubscribeData;
import com.greenfox.notification.model.classes.input.UnsubscribeInput;
import com.greenfox.notification.model.classes.output.Unsubscription;
import com.greenfox.notification.repository.UnsubscribeAttributeRepository;
import com.greenfox.notification.repository.UnsubscribeDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnsubscribeController {

  private final UnsubscribeDataRepository unsubscribeDataRepository;
  private final UnsubscribeAttributeRepository unsubscribeAttributeRepository;

  @Autowired
  public UnsubscribeController(UnsubscribeDataRepository unsubscribeDataRepository,
      UnsubscribeAttributeRepository unsubscribeAttributeRepository) {
    this.unsubscribeDataRepository = unsubscribeDataRepository;
    this.unsubscribeAttributeRepository = unsubscribeAttributeRepository;
  }

  @PostMapping("/unsubscriptions")
  public Unsubscription letUsersUnsubscribe(@RequestBody UnsubscribeInput unsubscribeInput) {
    UnsubscribeData unsubscribeData = unsubscribeInput.getData();
    UnsubscribeAttribute unsubscribeAttribute = unsubscribeData.getAttributes();
    unsubscribeAttributeRepository.save(unsubscribeAttribute);
    unsubscribeDataRepository.save(unsubscribeData);
    Link link = new Link();
    link.setSelf(System.getenv("HOSTNAME") + "/unsubscriptions/" + unsubscribeData.getId());
    Unsubscription unsubscription = new Unsubscription(link, unsubscribeData);
    return unsubscription;
  }
}
