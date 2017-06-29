package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.unsubscription.Errors;
import com.greenfox.notification.model.classes.unsubscription.Link;
import com.greenfox.notification.model.classes.unsubscription.UnsubscribeAttribute;
import com.greenfox.notification.model.classes.unsubscription.UnsubscribeData;
import com.greenfox.notification.model.classes.unsubscription.UnsubscribeInput;
import com.greenfox.notification.model.classes.unsubscription.Unsubscription;
import com.greenfox.notification.model.interfaces.Response;
import com.greenfox.notification.repository.UnsubscribeAttributeRepository;
import com.greenfox.notification.repository.UnsubscribeDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UnsubscriptionResponseService implements Response {
  private final SimpleDateService simpleDateService;
  private final UnsubscribeAttributeRepository unsubscribeAttributeRepository;
  private final UnsubscribeDataRepository unsubscribeDataRepository;
  private Link link;
  private Unsubscription unsubscription;
  private final Errors errors;


  @Autowired
  public UnsubscriptionResponseService(SimpleDateService simpleDateService,
      UnsubscribeAttributeRepository unsubscribeAttributeRepository,
      UnsubscribeDataRepository unsubscribeDataRepository, Errors errors) {
    this.simpleDateService = simpleDateService;
    this.unsubscribeAttributeRepository = unsubscribeAttributeRepository;
    this.unsubscribeDataRepository = unsubscribeDataRepository;
    this.link = new Link();
    this.errors = errors;
  }

  public Response letUsersUnsubscribe(UnsubscribeInput unsubscribeInput) {
    UnsubscribeData unsubscribeData = unsubscribeInput.getData();
    UnsubscribeAttribute unsubscribeAttribute = unsubscribeData.getAttributes();
    if (unsubscribeAttribute.getEmail() == null) {
      return errors;
    } else if (unsubscribeAttribute.getCreatedAt() == null) {
      unsubscribeAttribute.setCreatedAt(simpleDateService.getSimpleDateFormat());
    }
    unsubscribeAttributeRepository.save(unsubscribeAttribute);
    unsubscribeDataRepository.save(unsubscribeData);
    link.setSelf(System.getenv("HOSTNAME") + "/unsubscriptions/" + unsubscribeData.getId());
    unsubscription = new Unsubscription(link, unsubscribeData);
    return unsubscription;
  }
}
